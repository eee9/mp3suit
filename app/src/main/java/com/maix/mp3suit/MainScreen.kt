package com.maix.mp3suit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.maix.mp3suit.MainActivity.Companion.TAG
import com.maix.mp3suit.ui.theme.MxCyan


class MainScreen {

  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  val testMode = true
//  val testMode = false

  @Composable
  fun ShowMainScreen(main: MainActivity) {
    Column(modifier = Modifier
      .fillMaxSize()
      .background(MxCyan)
      .padding(3.dp)
    ) {
      Header(main)
      // Body
//      var largeText by remember { mutableStateOf("...") }
      OutlinedTextField(
        value = main.msgMainLog.value,
        onValueChange = { /* largeText = it */ },
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
  fun Header(main: MainActivity) {
    Text(
      text = main.version,
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
//    val setup = SetupScreen()
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
        val filename = main.chosenFileName?.value ?: main.fileForChoose
        val content = main.readFile(filename)

        if (testMode) {
          // Test button
          ButtonMx(onClick = {
            main.Toast("Test pressed")
            if (content.isNotEmpty()) {
              main.add2MainLog(content)
            } else {
              main.add2MainLog("No data in '$filename'")
            }
          }, enabled = main.showTestButton.value) {
            Text("Test")
          }

          // Translate button
          if (main.runTRANSLATE) {
            Button(
              onClick = {
                main.Toast("Translate pressed")
//              val translator = main.translator
                if (content.isEmpty()) {
                  main.add2MainLog("Nothing to translate in '$filename'")
                } else {
                  main.libTranslate.translateText(content)
                }
//              val langToUpper = translator.langToUpper
//              val msg = "[$langToUpper]: $translated"
//              Logd(msg)
//              main.add2MainLog(msg)
//              main.showSetupButton.value = !main.showSetupButton.value
              }, enabled = main.showTranslateButton.value
            ) {
              Text("Translate")
            }
          }
        }
        // Clean button
        Button(onClick = { main.msgMainLog.value = "" }) {
          Text("Clean")
        }

        // Tools button
        Button(onClick = { main.showToolDialog.value = true }) {
          Text("Tools")
        }
        if (main.showToolDialog.value) {
          main.toolScreen.ToolDialog()
        }

        // Setup button
        Button(onClick = { main.showSetupDialog.value = true }) {
          Text("Setup")
        }
        if (main.showSetupDialog.value) {
          main.setupScreen.SetupDialog()
        }

        // Exit button
        Button(onClick = { /* Exit */
          main.libMaix.closeApp(MainActivity())
        }) {
          Text("Exit")
        }
      }
    }
  }

  @Composable
  fun ButtonMx(onClick: () -> Unit, enabled: Boolean, content: @Composable RowScope.() -> Unit) {
    Button(
      onClick = onClick,
      enabled = enabled,
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Red, // Sets the background color
        contentColor = Color.White // Sets the text/content color
      ),
      content = content
    )
  }

}