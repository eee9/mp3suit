package com.maix.mp3suit

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.MxGreen
import com.maix.mp3suit.SetupScreen.Companion.MXPREF
import com.maix.mp3suit.SetupScreen.Companion.SUFFIX

class MainActivity: ComponentActivity() {

  val version = "mp3suit (ver 0.4.7, Q1T)"
  val LOGFILENAME = "mp3suit_log.txt"
  val NOTFOUNDMP3FILE = "_notfound2_mp3.txt"
  val NOTFOUNDLRCFILE = "_notfound2_lrc.txt"

  // !!! Turn OFF the service here for debug
//  val runSERVICE = true
    val runSERVICE = false

  companion object {
    const val TAG = "xMx3"
    val EOL = "\n"
    val SLASH = "/"

    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }

//    fun Logfull(msg: String, logfullpath: String) {
//      if (msg.isNotEmpty()) {
//        Logd(msg)
//        if (logfullpath.isNotEmpty()) {
//          val time = Maix().currTime()
//          FileIO().writeString2File(logfullpath, "$time: $msg$EOL", true)
//        }
//      }
//    }
//    var LOGFILEFULL = ""
//    fun Logdev(msg: String) { Logfull(msg, LOGFILEFULL) }
//    var NOTFOUNDMP3FULL = ""
//    fun LogNFmp3(msg: String) { Logfull(msg, NOTFOUNDMP3FULL) }
//    var NOTFOUNDLRCFULL = ""
//    fun LogNFlrc(msg: String) { Logfull(msg, NOTFOUNDLRCFULL) }
  }

  val libMaix = Maix()
  val libFileIO = FileIO()
  val libFileURI = FileURI()
  val setupScreen = SetupScreen()
  var context: Context? = null

  fun Toast(msg: String) {
    if (context != null) {
      val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
      toast.setGravity(Gravity.CENTER, 0, 0)
      toast.show()
    }
  }

  // for listen service
  override fun onDestroy() {
    super.onDestroy()
    Logd("Main OnDestroy")
    if (runSERVICE) libMaix.doUnbindService(this)
  }

  //==============================================================================================
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen
    Toast("MainActivity onCreate")
    val mainActivity = this

    if (runSERVICE) {
      libMaix.bindService(this)
      Logd("after bindService")
    }

    context = this.applicationContext
    context?.let {
      libFileURI.initMapExt(it)
    }

//    objSetupConf = SetupConf()
    setupScreen.Initializate(mainActivity)

    setContent {
//      PreviewSurfaceExample()
      Mp3suitTheme {
        Column(
          modifier = Modifier
            .background(MxGreen)
            .padding(8.dp)
        ) {
          setupScreen.showSetupDialog = rememberSaveable { mutableStateOf(false) }
          MainScreen().ShowMainScreen(mainActivity, setupScreen)
//          Button(onClick = {}) {
//            Text("Check")
//          }
        }
      }
    }
  }


  @Composable
  fun SurfaceExample() {
    Dialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false, // Crucial for full width
        decorFitsSystemWindows = false // Optional: allows drawing under system bars
      ),
      onDismissRequest = { }
    ) {
      Surface(
        modifier = Modifier
          .fillMaxWidth(0.99f)
          .fillMaxHeight(0.99f)
          .padding(1.dp), // Apply padding around the surface
//      shape = Shape., // Make the surface circular
        color = MaterialTheme.colorScheme.primary, // Use the primary theme color for background
        border = BorderStroke(1.dp, Color.Red), // Add a red border
        shadowElevation = 8.dp // Add a shadow (elevation)

      ) {
        Text(
          text = "Hello Surface!",
          modifier = Modifier.padding(16.dp), // Padding for the text inside the surface
          color = MaterialTheme.colorScheme.onPrimary // Text color that contrasts with the background
        )
      }
    }
  }

  @Composable
  fun PreviewSurfaceExample() {
    SurfaceExample()
  }

  //==============================================================================================
  // Initialize the ActivityResultLauncher
  var proxyState: MutableState<String>? = null
  var proxyKey: String = ""
  fun openDirectory0(key: String, text: MutableState<String>, uri: String) {
    if (text.value.isNotEmpty()) {
      proxyState = text
      proxyKey = key
      val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
      val uriSaved: String = sharedPreferences.getString(key + SUFFIX, null) ?: uri
      Logd("URI saved: '$uriSaved'")
      // WIERD !!! 777
      val initialUri: Uri = uriSaved.replace("/tree/", "/document/").toUri()
//      val initialUri = uriSaved.toUri()
      Logd("Input URI: '$uri'")
      Logd("Init. URI: '$initialUri'")
      openDocumentTreeLauncher.launch(initialUri)
    }
  }

  val openDocumentTreeLauncher: ActivityResultLauncher<Uri?> = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
      Logd("01 Main Activity registerForActivityResult")
      val path: String = uri.path  ?: "NO PATH"
      Logd("URI  : '$uri'")
      val decode: String = Uri.decode(uri.path)
      Logd("### Dec. : '$decode'")
      val lastPath = uri.lastPathSegment
      Logd("URI lastPath : '$lastPath'")
      Logd("Path  :  '$path'")
      val absolutePath = libFileURI.takeAbsolutePathFromUri(uri)
      val access = libFileIO.msgPathRights(absolutePath)
      Logd("Abs.  :  '$absolutePath' [$access]")

      proxyState?.value = absolutePath

      // save AbsolutePath and URI
      Logd("sharedPreferences saving...")
      val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
      sharedPreferences.edit { putString(proxyKey, absolutePath) }
      sharedPreferences.edit { putString(proxyKey + SUFFIX, uri.toString()) }
//      Logd("New $proxyKey: '$absolutePath' [$access]")
//      setupScreen.addMessage("New $proxyKey: '$absolutePath' [$access]")
//      Logd("New $proxyKey${SUFFIX}: '$uri'")
//      setupScreen.addMessage("New $proxyKey${SUFFIX}: '$uri'")
      Logd("... saving done.")
    }
  }

}
