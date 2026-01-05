package com.maix.mp3suit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {

  @Composable
  fun SimpleTextField() {
    // 1. Declare a mutable state to hold the user input.
    var text by remember { mutableStateOf("") } //

    // 2. Display the input field.

    TextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = text, // The current text to display.
      onValueChange = { newValue: String -> // Callback when the text changes.
        text = newValue // Update the state with the new value.
      },
      label = { Text("Enter your name") }, // Optional label/hint text.
      // other parameters like modifier, singleLine, etc.
    )
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
          Text(
            text = "Label",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = Color.Cyan          )
          SimpleTextField()
          SimpleTextField()
        }
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
              .background(Color.LightGray)
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(onClick = { /* Click 1 */ }) {
              Text("Button A")
            }
            Button(onClick = { /* Click 2 */ }) {
              Text("Button B")
            }
          }
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