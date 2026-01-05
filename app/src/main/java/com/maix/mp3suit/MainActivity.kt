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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maix.mp3suit.ui.theme.Mp3suitTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      Mp3suitTheme {
        Column(
          modifier = Modifier
            .fillMaxSize() // Fills the maximum available space
            .padding(16.dp),
          verticalArrangement = Arrangement.Bottom, // Pushes children to the bottom
          horizontalAlignment = Alignment.CenterHorizontally // Centers the child horizontally
        ) {
          // Other content can be added here, and it will be pushed up
          // above the button due to Arrangement.Bottom

          Row(
            modifier = Modifier
              .fillMaxWidth()
//              .background(Color.DarkGray)
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