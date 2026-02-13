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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
import com.maix.mp3suit.Translate.Keys.LANG_OF
import com.maix.mp3suit.Translate.Keys.LANG_TO
import com.maix.mp3suit.Translate.Keys.LAST_FILE
import com.maix.mp3suit.SetupScreen.Keys.MXPREF
import com.maix.mp3suit.SetupScreen.Keys.SUFFIX

class MainActivity: ComponentActivity() {

  val version = "mp3suit (ver 0.5.5, Q2D)"
  val LOGFILENAME = "mp3suit_log.txt"
  val NOTFOUNDMP3FILE = "_notfound2_mp3.txt"
  val NOTFOUNDLRCFILE = "_notfound2_lrc.txt"
  var fileForChoose = "/storage/emulated/0/xMx/77/79.txt"

  // !!! Turn OFF service for debug
//  val runSERVICE = true
    val runSERVICE = false
  // !!! Turn OFF translate for debug
  val runTRANSLATE = true
//  val runTRANSLATE = false

  companion object Const {
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
  val mainScreen = MainScreen(this)
  val setupScreen = SetupScreen(this)
  val toolScreen = ToolScreen(this)
  var libTranslate: Translate = Translate(this)
  var context: Context? = null

  fun Toast(msg: String) {
    if (context != null) {
      val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
      toast.setGravity(Gravity.CENTER, 0, 0)
      toast.show()
    }
  }

  fun readFile(filename: String): String {
    var res = ""
//    add2MainLog("Reading file '$filename'...")
    if (libFileIO.canReadPath(filename)) {
      res = libFileIO.readFile(filename)
//    } else {
//      add2MainLog("Can't read file '$filename'")
    }
    return res
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

    setContent {
//      Mp3suitTheme {
////              MainScreen().ShowScreen2(mainActivity)
//        showSetupDialog = rememberSaveable { mutableStateOf(false) }
//        msgMainLog = remember { mutableStateOf("MAIN LOG:$EOL") }
//        MainScreen().ShowMainScreen(mainActivity, setupScreen)
//      }
//        Column {
      showSetupDialog = rememberSaveable { mutableStateOf(false) }
      showSetupButton = rememberSaveable { mutableStateOf(true) }
      showTestButton = rememberSaveable { mutableStateOf(true) }
      showTranslateButton = rememberSaveable { mutableStateOf(false) }
      showToolDialog = rememberSaveable { mutableStateOf(false) }
      msgMainLog = remember { mutableStateOf("MAIN LOG:$EOL") }
      chosenFileName  = remember { mutableStateOf(fileForChoose) }

      if (runTRANSLATE) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
        val langOf = sharedPreferences.getString(LANG_OF, null) ?: "English"
        val langTo = sharedPreferences.getString(LANG_TO, null) ?: "French"
        val lastFilePath = sharedPreferences.getString(LAST_FILE, null) ?: fileForChoose
        chosenFileName.value = lastFilePath
        libTranslate.setupTranslator(langOf, langTo)
      }


      mainScreen.ShowMainScreen()
//      setupScreen.SetupDialog()
//      toolScreen.ToolDialog()
//        }
//      }
    }
  }

  lateinit var showSetupDialog: MutableState<Boolean>
  lateinit var showSetupButton: MutableState<Boolean>
  lateinit var showTestButton: MutableState<Boolean>
  lateinit var showTranslateButton: MutableState<Boolean>
  lateinit var showToolDialog: MutableState<Boolean>
  lateinit var msgMainLog: MutableState<String>
  var msgSetupLog: MutableState<String>? = null
  lateinit var chosenFileName: MutableState<String>

  fun add2SetupLog(msg: String) {
    Logd(msg)
    msgSetupLog?.value += msg + EOL
  }
  fun add2MainLog(msg: String) {
    Logd(msg)
    msgMainLog.value += msg + EOL
  }

  fun SaveInShared(key: String, value: String) {
    val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
    sharedPreferences.edit { putString(key, value) }
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
      Logd("New $proxyKey: '$absolutePath' [$access]")
      add2SetupLog("New $proxyKey: '$absolutePath' [$access]")
      Logd("New $proxyKey${SUFFIX}: '$uri'")
      add2SetupLog("New $proxyKey${SUFFIX}: '$uri'")
      add2MainLog("New $proxyKey${SUFFIX}: '$uri'")
      Logd("... saving done.")
    }
  }

}
