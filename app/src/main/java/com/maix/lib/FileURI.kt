package com.maix.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.IOException
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.indices
import kotlin.collections.set
import kotlin.text.isEmpty

class FileURI : AppCompatActivity() {

  companion object {
    const val EOL = "\n"
    const val SLASH = "/"
  }

  val TAG = "xMx lib.FileURI"

  private fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  private fun Loge(msg: String) {
    Log.e(TAG, "ERROR: $msg")
  }

  fun getRealPathFromURI(context: Context, contentUri: Uri): String {
    var res = ""
    var cursor: Cursor? = null
    try {
//      val proj1 = arrayOf<String?>(MediaStore.Images.Media.DATA)
      val proj2 = arrayOf<String?>(MediaStore.Audio.Media.DATA)
      cursor = context.contentResolver.query(contentUri, proj2, null, null, null)
      val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
      cursor.moveToFirst()
      res = cursor.getString(columnIndex)
    } catch (e: java.lang.Exception) {
      Loge("getRealPathFromURI Exception : $e")

    } finally {
      cursor?.close()
    }
    return res;
  }

  fun createFile() {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "text/plain" // Specify the MIME type for a text file
      putExtra(Intent.EXTRA_TITLE, "my_test_file.txt") // Suggested file name
    }
    createFileLauncher.launch(intent)
  }
  fun writeTextToFile(uri: Uri, text: String) {
    try {
      val outputStream = contentResolver.openOutputStream(uri)
      outputStream?.bufferedWriter().use { bw ->
        bw?.write(text)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private val createFileLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val uri: Uri? = result.data?.data
      uri?.let {
        // Now you have the Uri, you can write content to the file
        writeTextToFile(it, "MX This is the content of my new text file.")
      }
    }
  }

  //==============================================================================================
  private val MAP_EXT = HashMap<String, String>() // here are roots of external SDs
  private val SD0 = "primary"
  fun initMapExt(context: Context) {
    val extSD0: File = Environment.getExternalStorageDirectory()
    MAP_EXT[SD0] = extSD0.absolutePath
    val externalDirs = context.getExternalFilesDirs(null)
    if (externalDirs != null) {
      for (i in externalDirs.indices) {
        if (externalDirs[i] != null) {
          val path: String = externalDirs[i]!!.absolutePath
          val till = path.indexOf("/Android/data/")
          val rootPath = path.take(till)
          if (rootPath.compareTo(extSD0.absolutePath) == 0) continue
          val lastSlashPos = rootPath.lastIndexOf(SLASH)
          if (lastSlashPos > 0 && lastSlashPos < rootPath.length) {
            val rootKey = rootPath.substring(lastSlashPos + 1)
            if (rootKey.isNotEmpty()) MAP_EXT[rootKey] = rootPath
          }
        }
      }
    }
    Logd("MAP_EXT content:")
    MAP_EXT.forEach { (key, value) ->
      Logd("[$key] -> '$value'")
    }
    Logd(EOL)
  }

  private val COLON = ":"
  fun takeAbsolutePathFromUri(uri: Uri): String {
    var res = ""
    val lastPath: String = uri.lastPathSegment ?: ""
    val parts = lastPath.split(COLON)
    if (parts.size >= 2) {
      val head = MAP_EXT[parts[0]] ?: ""
      if (!head.isEmpty()) {
        res = head + SLASH + parts[1]
      }
    }

    return res
  }


  //==============================================================================================
  //==============================================================================================
  @SuppressLint("Range")
  private fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme != null) {
      if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        cursor?.close()
      }
    }
    if (result == null) {
      result = uri.path ?: ""
//    checkNotNull(result)
//    val cut = result.lastIndexOf('/')
//    if (cut != -1) {
//      result = result.substring(cut + 1)
//    }
    }
    return result
  }

  // Initialize the ActivityResultLauncher
  val openDocumentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
      // Get the Application Context

      // The user selected a directory. Handle the URI here.
      // You can persist this URI to grant long-term access.
//      contentResolver.takePersistableUriPermission(
//        uri,
//        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//      )

      // Now you can work with the selected directory using DocumentFile.fromTreeUri(this, uri)
      // For example, list files:
//      val documentTree = DocumentFile.fromTreeUri(this, uri)
//      documentTree?.listFiles()?.forEach { file: DocumentFile ->
////        Logd("File: ${file.name}, isDirectory: ${file.isDirectory}")
//        val p = file.canRead()
//        Logd("File: ${file.name}, isDirectory: ${file.isDirectory}")
//      }

    } else {
      // User cancelled the selection
    }
  }
  fun openDirectory() {
    // Optionally, specify an initial URI for the directory picker
    // For example, to open in the Downloads folder:
    // val initialUri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload")
    val initialUri: Uri? = null // No initial URI, opens at system default location
    openDocumentTreeLauncher.launch(initialUri)
  }

  //==============================================================================================
//  fun readFolderContent(context: Context, folderUri: Uri) {
  fun readFolderContent(folderUri: Uri, context: Context) {
    val contentResolver: ContentResolver = context.contentResolver

    // Create a DocumentFile from the folder's content URI
    val folderDocument = DocumentFile.fromTreeUri(context, folderUri)

    // Check if the URI represents a valid directory
    if (folderDocument != null && folderDocument.isDirectory) {
      // List the files and sub-folders within the directory
      val children = folderDocument.listFiles()

      for (child in children) {
        if (child.isDirectory) {
          // If it's a sub-folder, you can recursively call this function
          Logd("Sub-folder: ${child.name}")
          // readFolderContent(context, child.uri) // Uncomment for recursive reading
        } else {
          // If it's a file, you can access its properties
          Logd("File: ${child.name}, Size: ${child.length()} bytes")

          // To read the content of the file:
//          try {
//            contentResolver.openInputStream(child.uri)?.use { inputStream ->
//              // Read from the inputStream (e.g., convert to text or process bytes)
//              val content = inputStream.bufferedReader().use { it.readText() }
//              println_("File Content: $content")
//            }
//          } catch (e: Exception) {
//            println_("Error reading file ${child.name}: ${e.message}")
//          }
        }
      }
    } else {
      Logd("Invalid folder URI or not a directory.")
    }
  }
}