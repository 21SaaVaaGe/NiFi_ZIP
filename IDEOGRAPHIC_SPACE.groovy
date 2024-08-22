import java.io.File
import java.io.IOException

def mainDir = new File('C:/main directory') 
def tempDir = new File('C:/temp directory') 

if (!tempDir.exists()) {
  tempDir.mkdirs()
}

mainDir.eachFileRecurse { file ->
  if (file.isFile()) {
    def fileName = file.name.replaceAll(/[\u3000\s]+$/, '') // IDEOGRAPHIC SPACE
    def targetFile = new File(tempDir, fileName)
    file.withInputStream { input ->
      targetFile.withOutputStream { output ->
        output << input
      }
    }
    println "Copied file: ${file.path} to ${targetFile.path}"
  }
}
