package com.maix.mp3suit

import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maix.mp3suit.MainActivity.Companion.EOL
import com.maix.mp3suit.ui.theme.MxCyan

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
  lateinit var showSetupDialog: MutableState<Boolean>
  lateinit var msgSetupLog: MutableState<String>

  var _mp3path = "" // sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
  var _mp3uri  = "" // sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
  var _lrcpath = "" // sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
  var _lrcuri  = "" // sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
  var _logpath = "" // sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
  var _loguri  = "" // sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
  var _txtpath = "" // sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."
  var _txturi  = "" // sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."

  var _msgSetupLog = ""
  fun addMessage(msg: String) {
    Logd(msg)
    msgSetupLog.value += msg + EOL
  }

  fun Initializate(main: MainActivity) {
    Logd("Init starts.")
//    val libFileIO = main.libFileIO
    val sharedPreferences: SharedPreferences = main.getSharedPreferences(MXPREF, MODE_PRIVATE)
    _mp3path = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    _mp3uri = sharedPreferences.getString(KEYMP3 + SUFFIX, null) ?: "No MP3 uri found."
//    _lrcpath = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
//    _lrcuri = sharedPreferences.getString(KEYLRC + SUFFIX, null) ?: "No LRC uri found."
//    _logpath = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
//    _loguri = sharedPreferences.getString(KEYLOG + SUFFIX, null) ?: "No LOG uri found."
//    _txtpath = sharedPreferences.getString(KEYTXT, null) ?: "No TXT path found."
//    _txturi = sharedPreferences.getString(KEYTXT + SUFFIX, null) ?: "No TXT uri found."
//    val isService = if (main.runSERVICE) "ON" else "OFF"
//    val accessMp3: String = libFileIO.msgPathRights(_mp3path)
//    val accessLrc: String = libFileIO.msgPathRights(_lrcpath)
//    val accessLog: String = libFileIO.msgPathRights(_logpath)
//    val accessTxt: String = libFileIO.msgPathRights(_txtpath)
//    _msgSetupLog = "The service is $isService.$EOL"
//    _msgSetupLog += "MP3    : '$_mp3path' [$accessMp3]$EOL"
//    _msgSetupLog += "MP3 uri: '$_mp3uri'$EOL"
//    _msgSetupLog += "LRC    : '$_lrcpath' [$accessLrc]$EOL"
//    _msgSetupLog += "LRC uri: '$_lrcuri'$EOL"
//    _msgSetupLog += "LOG    : '$_logpath' [$accessLog]$EOL"
//    _msgSetupLog += "LOG uri: '$_loguri'$EOL"
//    _msgSetupLog += "TXT    : '$_txtpath' [$accessTxt]$EOL"
//    _msgSetupLog += "TXT uri: '$_txturi'$EOL"
    Logd("Init starts.")
  }

  @Composable
  fun SetupDialog(main: MainActivity, showSetupDialog: MutableState<Boolean>) {
    msgSetupLog = remember { mutableStateOf(_msgSetupLog) }
    mp3path = remember { mutableStateOf(_mp3path) }
    mp3uri  = remember { mutableStateOf(_mp3uri) }
//    lrcpath = remember { mutableStateOf(_lrcpath) }
//    lrcuri  = remember { mutableStateOf(_lrcuri) }
//    logpath = remember { mutableStateOf(_logpath) }
//    loguri  = remember { mutableStateOf(_loguri) }
//    txtpath = remember { mutableStateOf(_txtpath) }
//    txturi  = remember { mutableStateOf(_txturi) }

    Dialog(onDismissRequest = { showSetupDialog.value = false }) {
      Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Setup Configuration 5", style = MaterialTheme.typography.titleLarge)
          ChoosePath(main,"MP3:", KEYMP3, mp3path, mp3uri)
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
  fun ChoosePath(main: MainActivity, label: String, key: String, path: MutableState<String>, uri: MutableState<String>) {
    Button(
      onClick = {
        addMessage("Choosing...")
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