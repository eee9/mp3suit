package com.maix.mp3suit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.Box



class MainActivity : ComponentActivity() {

//  fun closeApp() {
//    this.moveTaskToBack(true)
//    Process.killProcess(Process.myPid())
//    exitProcess(1)
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()

    setContent {
      Mp3suitTheme {
//        RowExample()
//        BasicLayoutExample()
        BoxExample()
      }
    }
  }
}

@Composable
fun BoxExample() {
  Box(
    modifier = Modifier.size(100.dp), // Set a fixed size for the box
    contentAlignment = Alignment.Center // Center content within the box
  ) {
    // A background element
    Box(modifier = Modifier.matchParentSize().background(Color.Cyan)) {
      // This will be displayed first
    }
    // A text element on top of the background
    Text(text = "Hello", color = Color.Black)
  }
}

@Composable
fun TextMx(text: String) {
  Text(
    modifier = Modifier
      .background(Color.LightGray),
    text = "[$text]"
  )
}

@Composable
fun RowExample() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp), // Fill width and add padding
    horizontalArrangement = Arrangement.SpaceAround // Evenly distribute space horizontally
  ) {
    TextMx(text = "Left")
    TextMx(text = "Center")
    TextMx(text = "Right")
  }
}

@Composable
fun BasicLayoutExample() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Green)
      .padding(16.dp),
    verticalArrangement = Arrangement.Center, // Centers children vertically
    horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
  ) {
    Text("Item 1 in Column /Q16")
    Spacer(modifier = Modifier
      .height(16.dp)
      .background(Color.Red)
    )
    Row(
      modifier = Modifier
        .background(Color.Yellow)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround // Distributes children evenly with space
    ) {
      TextMx("Item A in Row")
      TextMx("Item B in Row")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Box(
      modifier = Modifier
        .background(Color.Cyan)
        .size(100.dp),
      contentAlignment = Alignment.Center // Centers child within the Box
    ) {
      Text("Box Content")
    }
  }
}
