package com.maix.mp3suit

import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maix.mp3suit.MainActivity.Companion.EOL
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.MxGreen

class SetupScreen: ComponentActivity() {
  val TAG = "xMx5"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  companion object {
    val MXPREF = "MXPREF2"
    val SUFFIX = "_URI"
    val KEYMP3 = "mp3path"
    val KEYLOG = "logpath"
    val KEYTXT = "textpath"
    val KEYLRC = "lyricpath"
  }

  lateinit var mp3path: MutableState<String>
  lateinit var mp3uri: MutableState<String>
  lateinit var lrcpath: MutableState<String>
  lateinit var lrcuri: MutableState<String>
  lateinit var logpath: MutableState<String>
  lateinit var loguri: MutableState<String>
  lateinit var txturi: MutableState<String>
  lateinit var txtpath: MutableState<String>

  @Composable
  fun SetupDialog(main: MainActivity, showSetupDialog: MutableState<Boolean>) {

    val sharedPreferences: SharedPreferences = main.getSharedPreferences(MXPREF, MODE_PRIVATE)
    val _mp3path = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    val _mp3uri = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
    val _lrcpath = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
    val _lrcuri  = sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
    val _logpath = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    val _loguri  = sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
    val _txtpath = sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."
    val _txturi  = sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."

    mp3path = remember { mutableStateOf(_mp3path) }
    mp3uri  = remember { mutableStateOf(_mp3uri) }
    lrcpath = remember { mutableStateOf(_lrcpath) }
    lrcuri  = remember { mutableStateOf(_lrcuri) }
    logpath = remember { mutableStateOf(_logpath) }
    loguri  = remember { mutableStateOf(_loguri) }
    txtpath = remember { mutableStateOf(_txtpath) }
    txturi  = remember { mutableStateOf(_txturi) }

    val accessMp3: String = main.libFileIO.msgPathRights(_mp3path)
    val accessLrc: String = main.libFileIO.msgPathRights(_lrcpath)
    val accessLog: String = main.libFileIO.msgPathRights(_logpath)
    val accessTxt: String = main.libFileIO.msgPathRights(_txtpath)

    var _msgSetupLog = "" // msgSetupLog.value + EOL
    val isService = if (main.runSERVICE) "ON" else "OFF"
    _msgSetupLog = "The service is $isService.$EOL"
    _msgSetupLog += "MP3    : '$_mp3path' [$accessMp3]$EOL"
    _msgSetupLog += "MP3 uri: '$_mp3uri'$EOL"
    _msgSetupLog += "LRC    : '$_lrcpath' [$accessLrc]$EOL"
    _msgSetupLog += "LRC uri: '$_lrcuri'$EOL"
    _msgSetupLog += "LOG    : '$_logpath' [$accessLog]$EOL"
    _msgSetupLog += "LOG uri: '$_loguri'$EOL"
    _msgSetupLog += "TXT    : '$_txtpath' [$accessTxt]$EOL"
    _msgSetupLog += "TXT uri: '$_txturi'$EOL"

    main.msgSetupLog = remember { mutableStateOf(_msgSetupLog) }

    Dialog(
      onDismissRequest = { showSetupDialog.value = false },
      properties = DialogProperties(
        usePlatformDefaultWidth = false, // Crucial for full width
        decorFitsSystemWindows = false // Optional: allows drawing under system bars
      ),
    ) {
      Surface(
        modifier = Modifier
          .fillMaxWidth(0.9f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier
          .fillMaxHeight(fraction = 0.9f)
          .background(MxGreen)
          .padding(16.dp)
        ) {
          Text("Setup Configuration", style = MaterialTheme.typography.titleLarge)
          ChoosePath(main,"MP3:", KEYMP3, mp3path, mp3uri)
          ChoosePath(main,"LRC:", KEYLRC, lrcpath, lrcuri)
          ChoosePath(main,"LOG:", KEYLOG, logpath, loguri)
          ChoosePath(main,"TXT:", KEYTXT, txtpath, txturi)
          OutlinedSetupLog(main, modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(1.dp))
          Spacer(modifier = Modifier.height(4.dp))
          Footer(main)
        }
      }
    }
  }

  @Composable
  fun OutlinedSetupLog(main: MainActivity, modifier: Modifier) {
    OutlinedTextField(
//          value = logMessage,
      value = main.msgSetupLog.value,
//        enabled = false,
      onValueChange = { main.addMessage(".") },
      textStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
//            fontFamily = FontFamily.Serif,
      ),
      label = { Text(" Setup LOG: ") },
      modifier = modifier,
//          modifier = Modifier
//            .fillMaxWidth()
////            .weight(1f)
//            .padding(1.dp),

      maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
//          colors = OutlinedTextFieldDefaults.colors(
//            disabledTextColor = Color.Blue,
//            disabledBorderColor = Color.Black,
//          )
    ) // OutlinedTextField(
  }

  @Composable
  fun Footer(main: MainActivity,) {
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
      Button( onClick = { main.addMessage("Check clicked.") } ) {
        Text("Add to log")
      }
      Button( onClick = { main.msgSetupLog.value = "" } ) {
        Text("Clear log")
      }
      Button( onClick = { main.Toast(main.msgSetupLog.value) } ) {
        Text("Show...")
      }
      Button(onClick = {
        main.showSetupDialog.value = false
      }) {
        Text(" OK ")
      }
//      Button( onClick = { libMaix.closeApp(MainActivity()) } ) {
//        Text(setupCloseText)
//      }
    }
  }

  @Composable
  fun ChoosePath(main: MainActivity, label: String, key: String, path: MutableState<String>, uri: MutableState<String>) {
    Button(
      onClick = {
        main.addMessage("Change:")
        main.openDirectory0(key, path, uri.value)
//        openDirectory(main, key, path, uri.value)
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

}