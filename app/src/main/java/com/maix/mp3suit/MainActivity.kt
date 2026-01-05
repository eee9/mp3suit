package com.maix.mp3suit

import android.app.Activity
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.autofill.ContentDataType.Companion.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlin.system.exitProcess
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {

  fun closeApp() {
    this.moveTaskToBack(true)
    Process.killProcess(Process.myPid())
    exitProcess(1)
  }

  @Composable
  fun SimpleTextField() {
    // 1. Declare a mutable state to hold the user input.
    var text by remember { mutableStateOf("") } //

    // 2. Display the input field.

    TextField(
      modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
      value = text, // The current text to display.
      onValueChange = { newValue: String -> // Callback when the text changes.
        text = newValue // Update the state with the new value.
      },
      label = { Text("Enter your name") }, // Optional label/hint text.
      // other parameters like modifier, singleLine, etc.
    )
  }

  @Composable
  fun SimpleEditTextFieldExample() {
    // 1. Define the state for the text field
    var text by remember { mutableStateOf("Initial Text") }

    // 2. Use the OutlinedTextField composable
    OutlinedTextField(
      modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
      value = text, // The current value to display
      onValueChange = { newValue ->
        text = newValue // Update the state when the user changes the input
      },
      label = { Text("Enter Name") }, // A label/hint
      // You can add other parameters here, e.g., modifier, keyboardOptions, etc.
    )
  }

  @Composable
  fun Header() {
    Text(
      text = "mp3suit, ver 0.0.0, Q15 (mutable)",
      modifier = Modifier
//        .size(240.dp)
        .fillMaxWidth(),

      textAlign = TextAlign.Center,
      fontSize = 24.sp,
//      fontSize = 24.dp,
      fontFamily = FontFamily.SansSerif
//      color = Color.Cyan
    )
  }

  @Composable
  fun Footer() {
    Column(
      modifier = Modifier
        .fillMaxSize() // Fills the maximum available space
        .padding(16.dp),
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
        Button(onClick = { /* Click 1 */ }) {
          Text("Test...")
        }
        Button(onClick = { closeApp() }) {
          Text("Exit")
        }
      }
    }
  }

  @Composable
  fun ScrollableTextFieldScreen() {
    // 1. Remember the scroll state for the Column
    val scrollState = rememberScrollState()

    // 2. Use Column with verticalScroll modifier
    Column(
      modifier = Modifier
        .fillMaxSize()
        // Add IME padding to automatically handle the keyboard
//        .windowInsetsPadding(WindowInsets.imePadding())
        .verticalScroll(scrollState)
        .padding(16.dp)
    ) {
      // Add some introductory text and space
      Text("Please enter your details below:", style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(16.dp))

      // Example TextFields
      for (i in 1..5) {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
          value = text,
          onValueChange = { text = it },
          label = { Text("Field $i") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
      }

      // A large multiline TextField
      var largeText by remember { mutableStateOf("") }
      OutlinedTextField(
        value = largeText,
        onValueChange = { largeText = it },
        label = { Text("Large Input Field") },
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp), // Fixed height for a larger input area
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
      )
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()

    setContent {
      Mp3suitTheme {
        Column(
          modifier = Modifier
            .fillMaxSize() // Fills the maximum available space
//            .fillMaxWidth()
            .background(Color.Yellow)
            .padding(2.dp),
          verticalArrangement = Arrangement.Top, // Pushes children to the bottom
          horizontalAlignment = Alignment.Start // Centers the child horizontally
        ) {
          Header()
          SimpleTextField()
          SimpleEditTextFieldExample()
          SimpleTextField()
          ScrollableTextFieldScreen()
          Footer()
        }

      }
    }


//
//
////        Column(
////          modifier = Modifier
////            .fillMaxSize() // Fills the maximum available space
////            .padding(16.dp),
////          verticalArrangement = Arrangement.Bottom, // Pushes children to the bottom
////          horizontalAlignment = Alignment.CenterHorizontally // Centers the child horizontally
////        ) {
////          TextField(
////            value = text,
//////            onValueChange = {
//////              if (it.length <= maxChar) text = it
//////            },
////            modifier = Modifier
////              .fillMaxWidth()
////              .padding(vertical = 4.dp),
////            shape = RoundedCornerShape(8.dp),
////            colors = TextFieldDefaults.textFieldColors(
////              backgroundColor = Color.LightGray,
////            focusedIndicatorColor =  Color.Transparent, //hide the indicator
////            unfocusedIndicatorColor = Color.Cyan)
////          )
////          Text(
////            text = "Label",
////            modifier = Modifier.fillMaxWidth(),
////            textAlign = TextAlign.Start,
////            color = Color.Cyan          )
////          Row(
////            modifier = Modifier
////              .fillMaxWidth()
////              .background(Color.LightGray)
////              .padding(16.dp),
////            horizontalArrangement = Arrangement.SpaceEvenly,
////            verticalAlignment = Alignment.CenterVertically
////          ) {
////            Button(onClick = { /* Click 1 */ }) {
////              Text("Button A")
////            }
////            Button(onClick = { /* Click 2 */ }) {
////              Text("Button B")
////            }
////          }
////        }
//      }
//    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  Mp3suitTheme {
    Greeting("Android")
  }
}