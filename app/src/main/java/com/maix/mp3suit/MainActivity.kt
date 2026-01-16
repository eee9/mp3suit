package com.maix.mp3suit

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.maix.mp3suit.ui.theme.Mp3suitTheme

class MainActivity : ComponentActivity() {

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  lateinit var context: Context
  fun Toast(msg: String) {
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen
    context = this.applicationContext
    Toast("MainActivity onCreate")

    val dataView = CounterViewModel()
    val ui = Screen()
    val t = uitests1()
    val u = uitests2()

    ui.initContext(context)  // for Toast
//    t.initContext(context)  // for Toast
    u.initContext(context)  // for Toast

    setContent {
      Mp3suitTheme {
//        ui.MainScreen()
//        t.ThreeDotsMenuExample()
//        t.ScrollableTextFieldScreen()
//        Input()
//        PressableText()
        u.ShowDialog2(dataView)
      }
    }
  }

  @Composable
  fun Input() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Input field") },
      modifier = Modifier.fillMaxWidth()
    )
  }

  @Composable
  fun PressableText() {
    var isPressed by remember { mutableStateOf(false) }
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .pointerInput(Unit) {
          detectTapGestures(
            onPress = {
              isPressed = true
              try {
                awaitRelease()
              } finally {
                isPressed = false
                Toast("Text pressed.")
              }
            }
            // onLongPress, onTap, onDoubleTap can also be used here
          )
        }
        .background(
          color = if (isPressed) Color.Red else Color.Green,
          shape = MaterialTheme.shapes.small
        )
        .padding(16.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = if (isPressed) "Pressed!" else "Press me",
        color = Color.White
      )
    }
  }

}
