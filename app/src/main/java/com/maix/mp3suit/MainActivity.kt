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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.MxGreen

class MainActivity: ComponentActivity() {

  val version = " (ver 0.4.1, Q1K)"
  val setupCloseText = "Exit /Q1K.r02"
  val LOGFILENAME = "mp3suit_log.txt"
  val NOTFOUNDMP3FILE = "_notfound2_mp3.txt"
  val NOTFOUNDLRCFILE = "_notfound2_lrc.txt"

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  val EOL = "\n"
  val SLASH = "/"

  val libMaix = Maix()
  val libFileIO = FileIO()
  var context: Context? = null

  fun Toast(msg: String) {
    if (context != null) {
      val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
      toast.setGravity(Gravity.CENTER, 0, 0)
      toast.show()
    }
  }

  // !!! Turn OFF the service here for debug
  private val runSERVICE = true
  //  private val runSERVICE = false

  val MXPREF = "MXPREF2"
  val SUFFIX = "_URI"
  val KEYMP3 = "mp3path"
  val KEYLOG = "logpath"
  val KEYTXT = "textpath"
  val KEYLRC = "lyricpath"

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen

    Toast("MainActivity onCreate")

    if (runSERVICE) {
      libMaix.bindService(this)
      Logd("after bindService")
    }

//    dataView = TestViewModel()
    val mainScreen = MainScreen()
    context = this.applicationContext
    mainScreen.initContext(context)  // for Toast

    val setupConf = SetupConf()

    setContent {
      Mp3suitTheme {
//        mainScreen.ShowMainScreen()
        SetupDialog(setupConf)
      }
    }
  }

  // for listen service
  override fun onDestroy() {
    super.onDestroy()
    Logd("Main OnDestroy")
    if (runSERVICE) libMaix.doUnbindService(this)
  }

  @Composable
  fun CloseButton() {
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .padding(20.dp),
      onClick = { libMaix.closeApp(MainActivity()) }

    ) {
      Text(setupCloseText)
    }
  }

  @Composable
  fun SetupDialog(setupConf: SetupConf) {
    var logMessage by remember { mutableStateOf(setupConf.msgSetup) }
    fun addMessage(msg: String) {
      logMessage.value += msg + EOL
    }
    val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
//    val pathMp3 = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
//    val pathText = sharedPreferences.getString(KEYTEXT, null) ?: "No path for TEXT found."
//    val pathLyric = sharedPreferences.getString(KEYLRC, null) ?: "No path for LYRIC found."
//    val pathLog = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    var _mp3path by remember { mutableStateOf(setupConf.pathMp3) }
    var _lrcpath by remember { mutableStateOf(setupConf.pathLrc) }
    var _logpath by remember { mutableStateOf(setupConf.pathLog) }
    var _txtpath by remember { mutableStateOf(setupConf.pathTxt) }
    _mp3path.value = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    _lrcpath.value = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
    _logpath.value = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    _txtpath.value = sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."

    var _mp3uri by remember { mutableStateOf(setupConf.uriMp3) }
    var _lrcuri by remember { mutableStateOf(setupConf.uriLrc) }
    var _loguri by remember { mutableStateOf(setupConf.uriLog) }
    var _txturi by remember { mutableStateOf(setupConf.uriTxt) }
    _mp3uri.value = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
    _loguri.value = sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
    _lrcuri.value = sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
    _txturi.value = sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."

    val accessMp3: String = libFileIO.msgPathRights(_mp3path.value)
    val accessText: String = libFileIO.msgPathRights(_txtpath.value)
    val accessLyric: String = libFileIO.msgPathRights(_lrcpath.value)
    val accessLog: String = libFileIO.msgPathRights(_logpath.value)


    val isService = if (runSERVICE) "ON" else "OFF"
    addMessage("The service is $isService.")
    addMessage("MP3    : $_mp3path [$accessMp3]")
    addMessage("MP3 uri: $_mp3uri")
    addMessage("LRC    : $_lrcpath [$accessLyric]")
    addMessage("LRC uri: $_lrcuri")
    addMessage("LOG    : $_logpath [$accessLog]")
    addMessage("LOG uri: $_loguri")
    addMessage("TXT    : $_txtpath [$accessText]")
    addMessage("TXT uri: $_txturi")

    Column(
      modifier = Modifier
        .background(MxGreen)
        .padding(10.dp)
    ) {
      ChoosePath("MP3:", KEYMP3, _mp3path)
      ChoosePath("LRC:", KEYLRC, _lrcpath)
      ChoosePath("LOG:", KEYLOG, _logpath)
      ChoosePath("TXT:", KEYTXT, _txtpath)
      // Body
//      var largeText by remember { mutableStateOf("...") }
      OutlinedTextField(
        value = logMessage.value,
//        enabled = false,
        onValueChange = { },
        textStyle = TextStyle(fontSize = 14.sp),
        label = { Text(" Setup LOG: ", color = Color.Black) },
        modifier = Modifier
//          .background(Color.LightGray)
          .fillMaxWidth()
          .padding(1.dp)
          .weight(1f),
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
        colors = OutlinedTextFieldDefaults.colors(
//          focusedBorderColor = Color.Blue,   // Color when the field is focused
//          unfocusedBorderColor = Color.Black, // Color when the field is not focused
          disabledTextColor = Color.Blue,
          disabledBorderColor = Color.Black,
          // You can also customize other colors here:
          // focusedLabelColor = Color.Green,
          // unfocusedLabelColor = Color.Red,
          // errorBorderColor = Color.Magenta
        )
      ) // Body
      CloseButton()
    }
  }

  @Composable
  fun ChoosePath(label: String, _key: String, _text: MutableState<String>) {
    Button(
      onClick = {
        _text.value = "Choosing..."
        openDirectory(_key, _text)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
//        .padding(horizontal = 10.dp)
    ) {
      Text(text = label)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Text(_text.value, color = MxCyan, modifier = Modifier.weight(1f),)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Icon(
        imageVector = Icons.Default.Edit, // .LocationOn,
        contentDescription = "Edit"
      )
    }
  }

  lateinit var proxyState: MutableState<String>
  var proxyKey: String = ""
  fun openDirectory(key: String, text:  MutableState<String>) {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
    proxyState = text
    proxyKey = key
  }
  val openDocumentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
      val path: String = uri.path ?: "NO PATH"
      Logd("URI  : '$uri'")
      val decode: String = Uri.decode(uri.path)
      Logd("### Dec. : '$decode'")
      val lastPath = uri.lastPathSegment
      Logd("URI lastPath : '$lastPath'")
      Logd("Path  :  '$path'")
      Logd("... saving done.")
      if (path.isNotEmpty()) {
        proxyState.value = path
        if (proxyKey.isNotEmpty()) {
          val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
          sharedPreferences.edit { putString(proxyKey, path) }
        }
      }
    }
  }

}
