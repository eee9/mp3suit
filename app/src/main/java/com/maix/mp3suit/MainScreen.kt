package com.maix.mp3suit

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.maix.mp3suit.MainActivity.Companion.KEYMP3
import com.maix.mp3suit.MainActivity.Companion.Logd
import com.maix.mp3suit.MainActivity.Companion.MXPREF
import com.maix.mp3suit.MainActivity.Companion.SUFFIX
import com.maix.mp3suit.ui.theme.MxCyan


class MainScreen {

  @Composable
  fun ShowMainScreen(main: MainActivity) {
    Column(modifier = Modifier
      .fillMaxSize()
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
        )
      ) // Body
      Footer(main)
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
  fun Footer(main: MainActivity) {
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
          main.Toast("Test pressed")
//          openDirectory()
        }) {
          Text("Test...")
        }
        Button(onClick = { }) {
          Text("Setup")
//          mainActivity.SetupDialog()
        }
        Button(onClick = { main.showSetupDialog.value = true }) {
          Text("Setup 5")
        }
        if (main.showSetupDialog.value) {
          Dialog5(main, main.showSetupDialog)
//          Dialog7(showSetupDialog)
//          SetupDialog()
        }
        Button(onClick = { /* Exit */
//          MainActivity().moveTaskToBack(true)
//          Process.killProcess(Process.myPid())
//          exitProcess(1)
          main.libMaix.closeApp(MainActivity())
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
  fun Dialog5(main: MainActivity, showSetupDialog: MutableState<Boolean>) {
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
        main.openDirectory(key, path, uri.value)
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
//  fun openDirectory(main: MainActivity, key: String, text: MutableState<String>, uri: String) {
//    if (text.value.isNotEmpty()) {
//      proxyState = text
//      proxyKey = key
//      val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
//      val uriSaved: String = sharedPreferences.getString(key + SUFFIX, null) ?: uri
//      Logd("URI saved: '$uriSaved'")
//      // WIERD !!! 777
//      val initialUri: Uri = uriSaved.replace("/tree/", "/document/").toUri()
////      val initialUri = uriSaved.toUri()
//      Logd("Input URI: '$uri'")
//      Logd("Init. URI: '$initialUri'")
//      main.openDocumentTreeLauncher.launch(initialUri)
//    }
//  }
}