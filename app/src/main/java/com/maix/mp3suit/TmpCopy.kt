package com.maix.mp3suit

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.maix.lib.Maix
import com.maix.mp3suit.MainActivity.Companion.KEYLOG
import com.maix.mp3suit.MainActivity.Companion.KEYLRC
import com.maix.mp3suit.MainActivity.Companion.KEYMP3
import com.maix.mp3suit.MainActivity.Companion.KEYTXT
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.MxGreen

class TmpCopy {

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  // Local Toast. !!! initContext must be run from MainActivity
  var context: Context? = null
  fun initContext(context: Context?) {
    this.context = context?.applicationContext
  }
  fun Toast(msg: String) {
    if (context != null)
      Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
  }

  val libMaix = Maix()

  @Composable
  fun ShowMainScreen2() {
//    MainActivity().SetupDialog()
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
      text = "mp3suit  (ver. 0.0.2, Q1D)",
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

  @Composable
  fun TextMx(text: String) {
    Text(
      modifier = Modifier
        .height(50.dp)
        .background(Color.LightGray),
      text = " [ $text ] "
    )
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
  fun Footer0() {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
//      Button(
////        enabled = false,
//        colors = ButtonDefaults.buttonColors(
//          disabledContainerColor = Color.Gray, // Color when disabled
//          disabledContentColor = Color.DarkGray // Content color when disabled
//        ),
//        onClick = { Toast(version) }
//      ) {
//        Text(version)
//      }
//      Button( onClick = { addMessage("Check clicked.") } ) {
//        Text("Add to log")
//      }
//      Button( onClick = { msgSetupLog.value = "" } ) {
//        Text("Clear log")
//      }
//      Button( onClick = { Toast(msgSetupLog.value) } ) {
//        Text("Show...")
//      }
//      Button( onClick = { libMaix.closeApp(MainActivity()) } ) {
//        Text(setupCloseText)
//      }
    }
  }

  @Composable
  fun SetupWindowExample() {
//    showSetupDialog = rememberSaveable { mutableStateOf(false) }
//
//    Button(onClick = { showSetupDialog.value = true }) {
//      Text("Open Setup 5")
//    }
//    if (showSetupDialog.value) {
//      Dialog5(showSetupDialog)
//    }
  }



  @Composable
  fun Dialog7(showSetupDialog: MutableState<Boolean>) {
    SetupDialog()
  }

  @Composable
  fun ChoosePath_(label: String, key: String, path: MutableState<String>, uri: String) {
    Button(
      onClick = {
        path.value = "Choosing..."
//        openDirectory(key, path, uri)
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
  fun SetupDialog() {
    Column(
      modifier = Modifier
        .background(MxGreen)
        .padding(8.dp)
    ) {

//      ChoosePath("MP3:", KEYMP3, mp3path, mp3uri)
//      ChoosePath("LRC:", KEYLRC, lrcpath, lrcuri)
//      ChoosePath("LOG:", KEYLOG, logpath, loguri)
//      ChoosePath("TXT:", KEYTXT, txtpath, txturi)
      OutlinedTextField(
//        value = msgSetupLog.value,
        value = "",
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


}