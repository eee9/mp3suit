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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.MxGreen

class MainActivity: ComponentActivity() {

  val version = "mp3suit (ver 0.4.2, Q1M)"
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
//    fun addMessage2(msg: String) {
//      objSetupConf?.msgSetup?.value += msg + "\n"
//    }

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

  var callClear: (String) -> Unit = {}
  lateinit var msgSetupLog: MutableState<String>

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen

    Toast("MainActivity onCreate")

    if (runSERVICE) {
      libMaix.bindService(this)
      Logd("after bindService")
    }

//    dataView = TestViewModel()
    context = this.applicationContext
    val mainScreen = MainScreen()
    val u2 = uitests2()
    context?.let {
      mainScreen.initContext(it)  // for Toast
      u2.initContext(it)  // for Toast
      libFileURI.initMapExt(it)
    }

    objSetupConf = SetupConf()
    setContent {
      Mp3suitTheme {
//        u2.SimpleTextFieldExample()
//        mainScreen.ShowMainScreen()
        val mp3path: MutableState<String> = remember { mutableStateOf("") }
        val mp3uri: MutableState<String> = remember { mutableStateOf("") }
        SetupDialog(mp3uri)
      }
    }
  }

  // for listen service
  override fun onDestroy() {
    super.onDestroy()
    Logd("Main OnDestroy")
    if (runSERVICE) libMaix.doUnbindService(this)
  }

  lateinit var textSetupLog: MutableState<String>
  fun addMessage(msg: String) {
    textSetupLog.value += EOL + msg
  }
  fun addMessage2(m:  MutableState<String>, msg: String) {
    m.value += EOL + msg
  }

  @Composable
  fun SetupDialog(mp3uri: MutableState<String>) {
    val isService = if (runSERVICE) "ON" else "OFF"
    textSetupLog = remember { mutableStateOf("The service is $isService.") } // The initial value is an empty string
    val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
    val _mp3path: String = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    mp3uri.value = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."


//    var textSetupLog2 by remember { mutableStateOf("") }
//    val accessMp3: String = libFileIO.msgPathRights(mp3path.value)
//
//    addMessage("MP3    : '${mp3path.value}' [$accessMp3]")
//    addMessage("MP3 uri: '${mp3uri.value}'")
    val textSetupLog2 = "MP3 uri: '${mp3uri.value}'"
    Column(
      modifier = Modifier
        .background(MxGreen)
        .padding(8.dp)
    ) {

//      ChoosePath("MP3:",KEYMP3, mp3path, mp3uri)
      OutlinedTextField(
        value = textSetupLog2,
//        onValueChange = { textSetupLog2 = it },
        onValueChange = {  },
        label = { Text (" Setup LOG: ") },
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
        modifier = Modifier
          .fillMaxWidth()
          .padding(1.dp)
          .weight(1f)
      )
      Button( onClick = { mp3uri.value = "" } ) {
        Text("Check...")
      }
//      Footer(mp3path)
    }
  }

  @Composable
  fun Footer(t: MutableState<String>) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
//        enabled = false,
        colors = ButtonDefaults.buttonColors(
          disabledContainerColor = Color.Gray, // Color when disabled
          disabledContentColor = Color.DarkGray // Content color when disabled
        ),
        onClick = { Toast(version) }
      ) {
        Text(version)
      }
      Button( onClick = { textSetupLog.value += EOL + "Check clicked." } ) {
        Text("Check...")
      }
      Button( onClick = { textSetupLog.value = "" } ) {
        Text("Clear log")
      }
      Button( onClick = { Toast(t.value) } ) {
        Text("Show...")
      }
      Button( onClick = { libMaix.closeApp(MainActivity()) } ) {
        Text(setupCloseText)
      }
    }
  }

//  @Composable
//  fun SetupDialog1() {
//    objSetupConf?.let { setupConf ->
////      var logMessage by remember { mutableStateOf(setupConf.msgSetup) }
////      var logMessage = remember { mutableStateOf(" @Setup LOG@ :") }
////      var msgSetupLog = ""
//      msgSetupLog = remember { mutableStateOf("") }
//      fun addMessage(msg: String) {
////        logMessage.value = logMessage.value + EOL + msg
//        msgSetupLog.value += EOL + msg
//      }
//      fun clearSetupLog() { msgSetupLog.value = "" }
////      clearSetupLog = ::addMessage
//
//      val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
//      var _mp3path by remember { mutableStateOf(setupConf.pathMp3) }
//      var _lrcpath by remember { mutableStateOf(setupConf.pathLrc) }
//      var _logpath by remember { mutableStateOf(setupConf.pathLog) }
//      var _txtpath by remember { mutableStateOf(setupConf.pathTxt) }
//      _mp3path.value = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
//      _lrcpath.value = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
//      _logpath.value = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
//      _txtpath.value = sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."
//
//      var _mp3uri by remember { mutableStateOf(setupConf.uriMp3) }
//      var _lrcuri by remember { mutableStateOf(setupConf.uriLrc) }
//      var _loguri by remember { mutableStateOf(setupConf.uriLog) }
//      var _txturi by remember { mutableStateOf(setupConf.uriTxt) }
//      _mp3uri.value = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
//      _lrcuri.value = sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
//      _loguri.value = sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
//      _txturi.value = sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."
//
//      val accessMp3: String = libFileIO.msgPathRights(_mp3path.value)
//      val accessLrc: String = libFileIO.msgPathRights(_lrcpath.value)
//      val accessLog: String = libFileIO.msgPathRights(_logpath.value)
//      val accessTxt: String = libFileIO.msgPathRights(_txtpath.value)
//
//      val isService = if (runSERVICE) "ON" else "OFF"
////      setupConf.addSetup("The service is $isService.")
////      setupConf.addSetup("MP3    : ${_mp3path.value} [$accessMp3]")
//      addMessage("The service is $isService.")
//      addMessage("MP3    : '${_mp3path.value}' [$accessMp3]")
//      addMessage("MP3 uri: '${_mp3uri.value}'")
//      addMessage("LRC    : '${_lrcpath.value}' [$accessLrc]")
//      addMessage("LRC uri: '${_lrcuri.value}'")
//      addMessage("LOG    : '${_logpath.value}' [$accessLog]")
//      addMessage("LOG uri: '${_loguri.value}'")
//      addMessage("TXT    : '${_txtpath.value}' [$accessTxt]")
//      addMessage("TXT uri: '${_txturi.value}'")
//
////      var logMessage = setupConf.msgSetup
//
//      Column(
//        modifier = Modifier
//          .background(MxGreen)
//          .padding(10.dp)
//      ) {
//        ChoosePath("MP3:", KEYMP3, _mp3path, _mp3uri)
//        ChoosePath("LRC:", KEYLRC, _lrcpath, _lrcuri)
//        ChoosePath("LOG:", KEYLOG, _logpath, _loguri)
//        ChoosePath("TXT:", KEYTXT, _txtpath, _txturi)
//        OutlinedTextField(
////          value = logMessage,
//          value = msgSetupLog.value,
////        enabled = false,
//          onValueChange = { addMessage(".") },
//          textStyle = TextStyle(
//            fontSize = 14.sp,
//            fontFamily = FontFamily.Monospace,
////            fontFamily = FontFamily.Serif,
//          ),
//          label = { Text(" Setup LOG: ", color = Color.Black) },
//          modifier = Modifier
//            .fillMaxWidth()
//            .padding(1.dp)
//            .weight(1f),
//          maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
//          colors = OutlinedTextFieldDefaults.colors(
//            disabledTextColor = Color.Blue,
//            disabledBorderColor = Color.Black,
//          )
//        ) // OutlinedTextField(
//        Footer()
//      }
//    }
//  }

  @Composable
  fun ChoosePath_(label: String, key: String, path: MutableState<String>, uri: String) {
    Button(
      onClick = {
        path.value = "Choosing..."
        openDirectory(key, path, uri)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
//        .padding(horizontal = 10.dp)
    ) {
      Text(text = label)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Text(path.value, color = MxCyan, modifier = Modifier.weight(1f),)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Icon(
        imageVector = Icons.Default.Edit, // .LocationOn,
        contentDescription = "Edit"
      )
    }
  }

  @Composable
  fun ChoosePath(label: String, key: String, path: MutableState<String>, uri: MutableState<String>) {
    Button(
      onClick = {
        path.value = "Choosing..."
        openDirectory(key, path, uri.value)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
//        .padding(horizontal = 10.dp)
    ) {
      Text(text = label)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Text(path.value, color = MxCyan, modifier = Modifier.weight(1f),)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Icon(
        imageVector = Icons.Default.Edit, // .LocationOn,
        contentDescription = "Edit"
      )
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

  val openDocumentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
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
      Logd("New $proxyKey${SUFFIX}: '$uri'")
      Logd("... saving done.")
    }
  }

}
