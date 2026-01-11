package com.maix.mp3suit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.uilib.*

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen

    val ui = uilib()
    setContent {
      Mp3suitTheme {
          ui.Screen2()
      }
    }
  }
}

