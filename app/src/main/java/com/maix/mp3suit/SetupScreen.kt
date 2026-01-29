package com.maix.mp3suit

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.core.net.toUri
import com.maix.mp3suit.MainActivity.Companion.KEYMP3
import com.maix.mp3suit.MainActivity.Companion.MXPREF
import com.maix.mp3suit.MainActivity.Companion.SUFFIX
import com.maix.mp3suit.ui.theme.MxCyan
import com.maix.mp3suit.ui.theme.MxGreen

class SetupScreen: ComponentActivity() {
  val TAG = "xMx5"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  @Composable
  fun SetupDialog(main: MainActivity, showSetupDialog: MutableState<Boolean>) {
    Dialog(onDismissRequest = { showSetupDialog.value = false }) {
      Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Setup Configuration 5", style = MaterialTheme.typography.titleLarge)
          ChoosePath(main,"MP3:", KEYMP3, main.mp3path, main.mp3uri)
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
        main.addMessage("Choosing...")
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

  //==============================================================================================
  // Initialize the ActivityResultLauncher
//  var proxyState: MutableState<String>? = null
//  var proxyKey: String = ""
  fun openDirectory(main: MainActivity, key: String, text: MutableState<String>, uri: String) {
    if (text.value.isNotEmpty()) {
      main.proxyState = text
      main.proxyKey = key
      val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
      val uriSaved: String = sharedPreferences.getString(key + SUFFIX, null) ?: uri
      Logd("URI saved: '$uriSaved'")
      // WIERD !!! 777
      val initialUri: Uri = uriSaved.replace("/tree/", "/document/").toUri()
//      val initialUri = uriSaved.toUri()
      Logd("57: Input URI: '$uri'")
      Logd("57: Init. URI: '$initialUri'")
      main.openDocumentTreeLauncher.launch(initialUri)
    }
  }

}