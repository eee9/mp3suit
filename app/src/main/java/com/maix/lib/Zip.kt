package com.maix.lib

import com.maix.mp3suit.MainActivity.Companion.Logd
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class Zip {
  //==============================================================================================
  fun readFileInZip(zipFile: String, file: String, outFile: String): Boolean {
    var res = false
    Logd("Zip archive -> '$zipFile'")
    if (File(zipFile).canRead()) {
      try {
        val zin = ZipInputStream(FileInputStream(zipFile))
        var entry: ZipEntry?
        var counter = 0
        while ((zin.getNextEntry().also { entry = it }) != null) {
          val name = entry!!.name
          counter++
//          Logd("$counter:   => '$name'")
          if (name.contains(file, true)) {
            Logd("Found in pos: [$counter]. Extrating...")
            res = extractToFile(zin, outFile)
            break
          }
        }
        zin.closeEntry()
      } catch (e: IOException) {
        Logd("Error with zip: $zipFile")
        e.printStackTrace()
      }
    }
    return res
  }

  private val BUFFER_SIZE = 4096
  private fun extractToFile(zipIn: ZipInputStream, filePath: String): Boolean {
    try {
      val bos = BufferedOutputStream(FileOutputStream(filePath))
      val bytesIn = ByteArray(BUFFER_SIZE)
      var read = 0
      while ((zipIn.read(bytesIn).also { read = it }) != -1) {
        bos.write(bytesIn, 0, read)
      }
      bos.close()
      return true
    } catch (e: IOException) {
      Logd("Error while extrating to '$filePath'")
      e.printStackTrace()
    }
    return false
  }
}