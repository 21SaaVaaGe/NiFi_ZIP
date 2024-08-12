import groovy.json.JsonSlurper
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response
import software.amazon.awssdk.services.s3.model.S3Object

def s3Client = S3Client.create {
    region = Region.US_WEST_2 // adjust to your region
    credentialsProvider = AwsCredentialsProvider.defaultProvider()
}

def bucketName = 'your-bucket-name'
def prefix = 'path/to/your/files/' // optional

def listObjectsRequest = ListObjectsV2Request.builder()
        .bucket(bucketName)
        .prefix(prefix)
        .build()

try {
    ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)

    def zipOutputStream = new ZipArchiveOutputStream(new ByteArrayOutputStream(1024 * 1024)) // 1MB buffer size
    def zipEntries = []

    listObjectsResponse.contents().each { S3Object obj ->
        def getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(obj.key())
                .build()

        def objectBytes = s3Client.getObject(getObjectRequest).bytes()
        def zipEntry = new ZipArchiveEntry(obj.key())
        zipEntry.setSize(objectBytes.length)
        zipEntries.add(zipEntry)
        zipOutputStream.putArchiveEntry(zipEntry)
        zipOutputStream.write(objectBytes)
        zipOutputStream.closeArchiveEntry()
    }

    zipOutputStream.close()

    def zipBytes = zipOutputStream.toByteArray()
    def flowFile = session.create()
    flowFile = session.write(flowFile, { output ->
        output.write(zipBytes)
    })

    flowFile.addAttribute('zip.archive.name', 'my_archive.zip') // add attribute with zip archive name
    flowFile.addAttribute('original.file.names', zipEntries.collect { it.name }) // add attribute with original file names

    session.transfer(flowFile, REL_SUCCESS)
} catch (Exception e) {
    log.error("Error creating zip archive: ${e.message}")
    session.transfer(flowFile, REL_FAILURE)
}