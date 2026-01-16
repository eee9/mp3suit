package com.maix.mp3suit

import android.content.Context
import android.os.Process
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.maix.mp3suit.ui.theme.Cyan

import com.maix.lib.Maix
import kotlin.system.exitProcess

class Screen {

  // Local Toast. !!! initContext must be run from MainActivity
  var context: Context? = null
  fun initContext(context: Context) {
    this.context = context.applicationContext
  }
  fun Toast(msg: String) {
    if (context != null)
      Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
  }

  val libMaix = Maix()
  @Composable
  fun MainScreen() {
    Column(modifier = Modifier
      .fillMaxSize()
//      .background(Color.Yellow)
      .background(Cyan)
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
        }) {
          Text("Test...")
        }
        Button(onClick = { /* Setting... */ }) {
          Text("Settings...")
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

}