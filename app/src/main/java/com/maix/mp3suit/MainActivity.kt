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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.MxGreen

class MainActivity: ComponentActivity() {

  val version = "mp3suit (ver 0.4.4, Q1Q)"
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
//          SetupDialog()

          showSetupDialog = rememberSaveable { mutableStateOf(false) }
//          u2.SetupWindowExample()
//          SetupWindowExample()
//          Dialog5(showSetupDialog)
          ShowMainScreen()
        }
      }
    }
  }

  @Composable
  fun SetupDialog() {
    Column(
      modifier = Modifier
        .background(MxGreen)
        .padding(8.dp)
    ) {

      ChoosePath("MP3:", KEYMP3, mp3path, mp3uri)
      ChoosePath("LRC:", KEYLRC, lrcpath, lrcuri)
      ChoosePath("LOG:", KEYLOG, logpath, loguri)
      ChoosePath("TXT:", KEYTXT, txtpath, txturi)
      OutlinedTextField(
        value = msgSetupLog.value,
        onValueChange = {},
        label = { Text (" Setup LOG: ") },
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
        colors = OutlinedTextFieldDefaults.colors(
          disabledTextColor = Color.Blue,
          disabledBorderColor = Color.Black,
        ),
        textStyle = TextStyle(
          fontSize = 14.sp,
          fontFamily = FontFamily.Monospace,
//          fontFamily = FontFamily.Serif,
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(1.dp)
          .weight(1f)
      )
      Footer()
    }
  }

  @Composable
  fun Footer0() {
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
      Button( onClick = { addMessage("Check clicked.") } ) {
        Text("Add to log")
      }
      Button( onClick = { msgSetupLog.value = "" } ) {
        Text("Clear log")
      }
      Button( onClick = { Toast(msgSetupLog.value) } ) {
        Text("Show...")
      }
      Button( onClick = { libMaix.closeApp(MainActivity()) } ) {
        Text(setupCloseText)
      }
    }
  }


  @Composable
  fun SetupWindowExample() {
    showSetupDialog = rememberSaveable { mutableStateOf(false) }

    Button(onClick = { showSetupDialog.value = true }) {
      Text("Open Setup 5")
    }
    if (showSetupDialog.value) {
      Dialog5(showSetupDialog)

    }
  }

  @Composable
  fun Dialog5(showSetupDialog: MutableState<Boolean>) {
    Dialog(onDismissRequest = { showSetupDialog.value = false }) {
      Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Setup Configuration 5", style = MaterialTheme.typography.titleLarge)
          ChoosePath("MP3:", KEYMP3, mp3path, mp3uri)
          Spacer(modifier = Modifier.height(16.dp))

          var setting1Enabled by remember { mutableStateOf(true) }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = setting1Enabled, onCheckedChange = { setting1Enabled = it })
            Text("Enable Feature 4")
          }
          Spacer(modifier = Modifier.height(36.dp))
          Button(onClick = {
            showSetupDialog.value = false
          }) {
            Text("Save and Close 4")
          }
        }
      }
    }
  }

  @Composable
  fun Dialog7(showSetupDialog: MutableState<Boolean>) {
      SetupDialog()
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
//        Footer(msgSetupLog)
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
        addMessage("Choosing...")
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

  @Composable
  fun ShowMainScreen() {
    Column(modifier = Modifier
      .fillMaxSize()
//      .background(Color.Yellow)
      .background(MxCyan)
      .padding(3.dp)
    ) {
      Header()
      // Body
      var largeText by remember { mutableStateOf("...") }
      OutlinedTextField(
        value = largeText,
        onValueChange = { largeText = it },
        label = { Text("Logging", color = Color.Black) },
        modifier = Modifier
//          .background(Color.LightGray)
          .fillMaxWidth()
          .padding(1.dp)
          .weight(1f),
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Blue,   // Color when the field is focused
          unfocusedBorderColor = Color.Black, // Color when the field is not focused
          // You can also customize other colors here:
          // focusedLabelColor = Color.Green,
          // unfocusedLabelColor = Color.Red,
          // errorBorderColor = Color.Magenta
        )
      ) // Body
      Footer()
    }
  }

  @Composable
  fun Header() {
    Text(
      text = "mp3suit  (ver. 0.0.3, Q1R)",
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth(),
//          .background(Color.LightGray),
//          .padding(vertical = 4.dp, horizontal = 16.dp),
      fontFamily = FontFamily(
        Font(
          DeviceFontFamilyName("sans-serif-smallcaps"),
          weight = FontWeight.Light
        )
      ),
      fontSize = 24.sp
    )
  }

  @Composable
  fun Footer() {
    Column(
      modifier = Modifier
//        .fillMaxSize() // Fills the maximum available space
        .padding(2.dp),
      verticalArrangement = Arrangement.Bottom, // Pushes children to the bottom
      horizontalAlignment = Alignment.CenterHorizontally // Centers the child horizontally
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
//          .background(Color.LightGray)
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        ButtonMx(onClick = { /* Test... */
          Toast("Test pressed")
//          openDirectory()
        }) {
          Text("Test...")
        }
        Button(onClick = { }) {
          Text("Setup")
//          mainActivity.SetupDialog()
        }
        Button(onClick = { showSetupDialog.value = true }) {
          Text("Setup 5")
        }
        if (showSetupDialog.value) {
          Dialog5(showSetupDialog)
//          Dialog7(showSetupDialog)
//          SetupDialog()
        }
        Button(onClick = { /* Exit */
//          MainActivity().moveTaskToBack(true)
//          Process.killProcess(Process.myPid())
//          exitProcess(1)
          libMaix.closeApp(MainActivity())
        }) {
          Text("Exit")
        }
//        Button(onClick = { /* Exit */ } (
//
//        ) {
//          Text("Exit")
//        }
      }
    }
  }

  @Composable
  fun ButtonMx(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Red, // Sets the background color
        contentColor = Color.White // Sets the text/content color
      ),
      content = content
    )
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
//      Logd("New $proxyKey: '$absolutePath' [$access]")
      addMessage("New $proxyKey: '$absolutePath' [$access]")
//      Logd("New $proxyKey${SUFFIX}: '$uri'")
      addMessage("New $proxyKey${SUFFIX}: '$uri'")
      Logd("... saving done.")
    }
  }

}
