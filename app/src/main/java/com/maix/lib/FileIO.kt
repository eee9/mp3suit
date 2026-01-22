package com.maix.lib

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class FileIO {

  val TAG = "xMx lib.FileIO"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  fun Loge(msg: String) {
    Log.e(TAG, "ERROR: $msg")
  }

  //==============================================================================================
  fun isPathExist(path: String): Boolean {
    return File(path).exists()
  }

  fun canReadPath(path: String): Boolean {
    val file = File(path)
    return if (file.exists()) file.canRead() else false
  }

  fun canWritePathExists(path: String): Boolean {
    val file = File(path)
    return if (file.exists()) file.canWrite() else false
  }

  fun canWritePath(path: String): Boolean {
    return File(path).canWrite()
  }

  fun renameFile(source: String, target: String): Boolean {
    var res = false
    val sourceFile = File(source)
    val targetFile = File(target)
    if (sourceFile.exists()) {
      // Attempt to rename the file
      if (sourceFile.renameTo(targetFile)) {
        res = true
      } else {
        Loge("Rename ERROR! Can't rename '$source' to '$target'.")
      }
    }
    return res
  }

  //==============================================================================================
  fun readFileAsAL(fileName: String): ArrayList<String> {
    val res = ArrayList<String>()
    if (!canReadPath(fileName)) return res

    var line: String
    var br: BufferedReader? = null
    try {
      br = BufferedReader(FileReader(File(fileName)))
      while ((br.readLine().also { line = it }) != null) {
        res.add(line)
      }
    } catch (e: FileNotFoundException) {
      Loge(e.toString())
    } catch (e: IOException) {
      Loge(e.toString())
    }
    return res
  }

  //==============================================================================================
  fun readFolderAsAL(path: String, absolute: Boolean = true): ArrayList<String> {
    val res = ArrayList<String>()
    val rootDir = File(path)
    if (rootDir.exists() && rootDir.isDirectory && rootDir.canRead()) {
      rootDir.walk().forEach { file ->
        if (absolute) res.add(file.absolutePath)
        else {
          if (file.absolutePath.length > path.length)
            res.add(file.absolutePath.substring(path.length + 1))
        }
      }
    }
    return res
  }

  //==============================================================================================
  fun showFolder(path: String) {
    val rootDir = File(path)
    if (rootDir.exists() && rootDir.isDirectory && rootDir.canRead()) {
      Logd("List of => [${rootDir.absolutePath}]:")
      rootDir.walk().forEach { file ->
        if (file.isDirectory) {
          Logd("D: [${file.absolutePath}]")
        } else if (file.isFile) {
          Logd("F: '${file.absolutePath}' (${file.length()}b)")
        } else {
          Logd("?: '${file.absolutePath}'")
        }
      }
    } else {
      Logd("Can't read path '${rootDir.absolutePath}'")
    }
    Logd("list is over.")
  }

  //==============================================================================================
  fun writeString2File(fileName: String, text: String, isAppend: Boolean): Boolean {
    var res = false
    val file = File(fileName)
    try {
      val writer = BufferedWriter(FileWriter(file, isAppend))
      writer.write(text)
      writer.flush()
      writer.close()
      res = true
    } catch (e: IOException) {
      Loge("Write to file ERROR: " + e.message)
    }
    return res
  }

  fun writeString2File2(fileName: String, text: String) {
    val file = File(fileName)
    try {
      if (!file.exists()) {
        file.createNewFile()
      }
      BufferedWriter(file.writer()).use { writer ->
        writer.write(text)
      }
    } catch (e: Exception) {
      Loge("Error creating or writing to file '$fileName': ${e.message}")
    }
  }

  //==============================================================================================
  private fun msgExist(file: File): String {
//  private fun msgExist(file: File) {
    val fileIs = file.exists()
    val existMsg = if (fileIs) " exists" else " NOT exists"
    val name = file.absolutePath
//    println_("'$name' -> $existMsg")
    return "'$name' -> $existMsg"
  }

  //==============================================================================================
  fun msgPathRights(path: String): String {
    return msgRights(File(path))
  }
  //  private fun msgRights(file: File) {
  fun msgRights(file: File): String {
    var res = ""
//    Logd(EOL + "Info for '${file.absolutePath}':")
//    var msg = msgExist(file)
    if (file.exists()) {
      res += "+"
      res += if (file.isDirectory) "D" else "d"
      res += if (file.isFile) "F" else "f"
      res += if (file.canRead()) "R" else "r"
      res += if (file.canWrite()) "W" else "w"
//      Logd("File attr: [$msg]")
    } else {
      res = "-"
    }
//    Logd(msg)
    return res
  }

}