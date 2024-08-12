AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        
S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
InputStream objectData = object.getObjectContent();
// Process the objectData stream.
objectData.close();



    
private final AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().build();

private Collection<String> loadFileFromS3() {
    try (final S3Object s3Object = amazonS3Client.getObject(BUCKET_NAME,
                                                            FILE_NAME);
        final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader)) {
        return reader.lines().collect(Collectors.toSet());
    } catch (final IOException e) {
        log.error(e.getMessage(), e)
        return Collections.emptySet();
    }
}

//Assuming the credentials are read from Environment Variables, so no hardcoding here

    S3Client client = S3Client.builder()
                        .region(regionSelected)
                        .build();
    
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
    
    ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(getObjectRequest);

    InputStream stream = new ByteArrayInputStream(responseInputStream.readAllBytes());
    
    
    System.out.println("Content :"+ new String(responseInputStream.readAllBytes(), StandardCharsets.UTF_8));
    
    
      BasicAWSCredentials awsCreds = new BasicAWSCredentials("accessKey", "secretKey");
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withRegion("region_name_here").build();  
2 >>>

   S3Object object = s3Client.getObject(new GetObjectRequest("bucketName", "key"));
3 >>>

   BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));

    String s = null;
    while ((s = reader.readLine()) != null)
    {
        System.out.println(s);
        //your business logic here
    }
    
    
    
    import java.util.zip.*
  
String zipFileName = "file.zip"  
String inputDir = "logs"
def outputDir = "zip"

//Zip files
  
ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))  
new File(inputDir).eachFile() { file -> 
  //check if file
  if (file.isFile()){
    zipFile.putNextEntry(new ZipEntry(file.name))
    def buffer = new byte[file.size()]  
    file.withInputStream { 
      zipFile.write(buffer, 0, it.read(buffer))  
    }  
    zipFile.closeEntry()
  }
}  
zipFile.close()  


//UnZip archive

byte[] buffer = new byte[1024]
ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName))
ZipEntry zipEntry = zis.getNextEntry()
while (zipEntry != null) {
     File newFile = new File(outputDir+ File.separator, zipEntry.name)
     if (zipEntry.isDirectory()) {
         if (!newFile.isDirectory() && !newFile.mkdirs()) {
             throw new IOException("Failed to create directory " + newFile)
         }
     } else {
         // fix for Windows-created archives
         File parent = newFile.parentFile
         if (!parent.isDirectory() && !parent.mkdirs()) {
             throw new IOException("Failed to create directory " + parent)
         }
         // write file content
         FileOutputStream fos = new FileOutputStream(newFile)
         int len = 0
         while ((len = zis.read(buffer)) > 0) {
             fos.write(buffer, 0, len)
         }
         fos.close()
     }
 zipEntry = zis.getNextEntry()
 }
zis.closeEntry()
zis.close()

 https://relentlesscoding.com/2017/08/04/using-groovys-antbuilder-to-zip-and-unzip-files/
     * https://stackoverflow.com/questions/645847/unzip-archive-with-groovy
     * @param zipFileName
     * @param outputDir
     * @return
     */
    static boolean unzipV2(String zipFileName, String outputDir) {
        try {
            def ant = new AntBuilder()
            ant.unzip(
                    src: zipFileName,
                    dest: outputDir,
                    overwrite: "true"
            )
            return true
        } catch (Exception e) {
            e.printStackTrace()
            return false
        }
    }
    
    
    import java.nio.file.*

//UnZip archive

def zip = new ZipFile(new File(outputDir + zipFileName))
zip.entries().each{  
  if (!it.isDirectory()){
    def fOut = new File(outputDir + File.separator + it.name)
    //create output dir if not exists
    new File(fOut.parent).mkdirs()

    InputStream is = zip.getInputStream(it);
    Files.copy(is, fOut.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }
}
zip.close()