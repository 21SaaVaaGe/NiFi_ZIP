import java.io.File
import java.io.IOException

def mainDir = new File('D:\\\\GroovyProjects\\Test directory\\Directory with whitespace\\test folder') // replace with your main directory path
def tempDir = new File('temp directory') 

if (!tempDir.exists()) {
  tempDir.mkdirs()
}

mainDir.eachFileRecurse { file ->
  if (file.isFile()) {
    def targetFile = new File(tempDir, file.name.trim())
    file.withInputStream { input ->
      targetFile.withOutputStream { output ->
        output << input
      }
    }
  }
}

import java.io.File

def mainDir = new File('C:/main directory') // replace with your main directory path
def tempDir = new File('C:/temp directory') // replace with your temp directory path

if (!tempDir.exists()) {
    tempDir.mkdirs()
}

mainDir.eachFileRecurse { file ->
    if (file.isFile()) {
        def fileName = file.name.replaceAll(/[\u3000\s]+$/, '') // remove trailing whitespaces and IDEOGRAPHIC SPACE
        def targetFile = new File(tempDir, fileName)
        file.renameTo(targetFile)
        println "Moved file: ${file.path} to ${targetFile.path}"
    }
}
