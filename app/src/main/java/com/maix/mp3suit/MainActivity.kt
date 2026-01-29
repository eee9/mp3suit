package com.maix.mp3suit

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.MxGreen

class MainActivity: ComponentActivity() {

  val version = "mp3suit (ver 0.4.5, Q1T)"
  val setupCloseText = "Exit"
  val LOGFILENAME = "mp3suit_log.txt"
  val NOTFOUNDMP3FILE = "_notfound2_mp3.txt"
  val NOTFOUNDLRCFILE = "_notfound2_lrc.txt"

  // !!! Turn OFF the service here for debug
//  private val runSERVICE = true
    private val runSERVICE = false

  companion object {
    const val TAG = "xMx3"
    val EOL = "\n"
    val SLASH = "/"

    val MXPREF = "MXPREF2"
    val SUFFIX = "_URI"
    val KEYMP3 = "mp3path"
    val KEYLOG = "logpath"
    val KEYTXT = "textpath"
    val KEYLRC = "lyricpath"

    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
    var objSetupConf: SetupConf? = null

    fun Logfull(msg: String, logfullpath: String) {
      if (msg.isNotEmpty()) {
        Logd(msg)
        if (logfullpath.isNotEmpty()) {
          val time = Maix().currTime()
          FileIO().writeString2File(logfullpath, "$time: $msg$EOL", true)
        }
      }
    }
    var LOGFILEFULL = ""
    fun Logdev(msg: String) { Logfull(msg, LOGFILEFULL) }
    var NOTFOUNDMP3FULL = ""
    fun LogNFmp3(msg: String) { Logfull(msg, NOTFOUNDMP3FULL) }
    var NOTFOUNDLRCFULL = ""
    fun LogNFlrc(msg: String) { Logfull(msg, NOTFOUNDLRCFULL) }
  }

  val libMaix = Maix()
  val libFileIO = FileIO()
  val libFileURI = FileURI()
  var context: Context? = null

  fun Toast(msg: String) {
    if (context != null) {
      val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
      toast.setGravity(Gravity.CENTER, 0, 0)
      toast.show()
    }
  }

  lateinit var msgSetupLog: MutableState<String>
  fun addMessage(msg: String) {
    Logd(msg)
    msgSetupLog.value += msg + EOL
  }
  lateinit var mp3path: MutableState<String>
  lateinit var mp3uri: MutableState<String>
  lateinit var lrcpath: MutableState<String>
  lateinit var lrcuri: MutableState<String>
  lateinit var logpath: MutableState<String>
  lateinit var loguri: MutableState<String>
  lateinit var txturi: MutableState<String>
  lateinit var txtpath: MutableState<String>
  lateinit var showSetupDialog: MutableState<Boolean>

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

    val activityLauncher = registerForActivityResult(MySecondActivityContract()) { result ->
      Logd("02 SecondActivity result: [$result]")
      // используем result
    }

    if (runSERVICE) {
      libMaix.bindService(this)
      Logd("after bindService")
    }

//    dataView = TestViewModel()
    context = this.applicationContext
//    val u2 = uitests2()
    context?.let {
//      mainScreen.initContext(it)  // for Toast
//      u2.initContext(it)  // for Toast
      libFileURI.initMapExt(it)
    }

    val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
    val _mp3path = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    val _mp3uri = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
    val _lrcpath = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
    val _lrcuri = sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
    val _logpath = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    val _loguri = sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
    val _txtpath = sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."
    val _txturi = sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."
    val isService = if (runSERVICE) "ON" else "OFF"
    val accessMp3: String = libFileIO.msgPathRights(_mp3path)
    val accessLrc: String = libFileIO.msgPathRights(_lrcpath)
    val accessLog: String = libFileIO.msgPathRights(_logpath)
    val accessTxt: String = libFileIO.msgPathRights(_txtpath)
    var _msgSetupLog = "The service is $isService.$EOL"
    _msgSetupLog += "MP3    : '$_mp3path' [$accessMp3]$EOL"
    _msgSetupLog += "MP3 uri: '$_mp3uri'$EOL"
    _msgSetupLog += "LRC    : '$_lrcpath' [$accessLrc]$EOL"
    _msgSetupLog += "LRC uri: '$_lrcuri'$EOL"
    _msgSetupLog += "LOG    : '$_logpath' [$accessLog]$EOL"
    _msgSetupLog += "LOG uri: '$_loguri'$EOL"
    _msgSetupLog += "TXT    : '$_txtpath' [$accessTxt]$EOL"
    _msgSetupLog += "TXT uri: '$_txturi'$EOL"

    objSetupConf = SetupConf()
    setContent {
      msgSetupLog = remember { mutableStateOf(_msgSetupLog) }
      mp3path = remember { mutableStateOf(_mp3path) }
      mp3uri  = remember { mutableStateOf(_mp3uri) }
      lrcpath = remember { mutableStateOf(_lrcpath) }
      lrcuri  = remember { mutableStateOf(_lrcuri) }
      logpath = remember { mutableStateOf(_logpath) }
      loguri  = remember { mutableStateOf(_loguri) }
      txtpath = remember { mutableStateOf(_txtpath) }
      txturi  = remember { mutableStateOf(_txturi) }
      Mp3suitTheme {
        Column(
          modifier = Modifier
            .background(MxGreen)
            .padding(8.dp)
        ) {
          showSetupDialog = rememberSaveable { mutableStateOf(false) }
//          SetupDialog()

//          u2.SetupWindowExample()
//          SetupWindowExample()
//          Dialog5(showSetupDialog)
          MainScreen().ShowMainScreen(mainActivity)
//          SetupScreen().Setup77(mainActivity)
//          Button( onClick = {
//            Logd("Start 2nd...")
//            SetupScreen().openDirectory()
////            activityLauncher.launch("What is the answer?")
//
//          } ) {
//
//            Text("2nd activity")
//          }
        }
      }
    }
  }

  //==============================================================================================
  // Initialize the ActivityResultLauncher
  var proxyState: MutableState<String>? = null
  var proxyKey: String = ""
  fun openDirectory(key: String, text: MutableState<String>, uri: String) {
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
      addMessage("New $proxyKey: '$absolutePath' [$access]")
//      Logd("New $proxyKey${SUFFIX}: '$uri'")
      addMessage("New $proxyKey${SUFFIX}: '$uri'")
      Logd("... saving done.")
    }
  }

}
