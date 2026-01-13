package com.maix.mp3suit

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.core.content.edit
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
  //  lateinit var context: Context
  fun Toast(msg: String) {
//      Toast
//        .makeText(context, msg, Toast.LENGTH_LONG)
//        .setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
//        .show()
//    if (context != null) {
//    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
    toast.show()
//    } else {
//      Log.d("xMx", "Context is NULL!")
//    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen

//    Toast("START 2...")
    val ui = uilib()
//    val ui = Screen()
    context = this.applicationContext
    ui.initContext(context)  // for Toast

//    context = MainActivity().applicationContext
    setContent {
      Mp3suitTheme {
        //        Click()
//        ui.MainScreen()
//        ui.Footer()
//        PressableTextExample()
//        ui.Input()
//        ui.ThreeDotsMenuExample()
        ui.SetupWindowExample()
      }
    }
  }

  private fun openDirectory() {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
  }

  val openDocumentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
      val path: String = uri.path  ?: "NO PATH"
      Logd("URI  : '$uri'")
      val decode: String = Uri.decode(uri.path)
      Logd("### Dec. : '$decode'")
      val lastPath = uri.lastPathSegment
      Logd("URI lastPath : '$lastPath'")
      Logd("Path  :  '$path'")
      Logd("... saving done.")
    }
  }

  @Composable
  fun Click() {
    var text by remember { mutableStateOf("") }
    Surface(onClick = { /* handle click */
      Toast("Click pressed.")
      text = "Text is changed"
//      openDirectory()
    }) {

      TextField(
        value = text,
        enabled = false,
        onValueChange = { text = it },
        label = { Text("Enter Name") }
      )
    }
  }

  @Composable
  fun Icon3() {
    Box(
      modifier = Modifier
//      .fillMaxSize()
        .wrapContentSize(Alignment.TopEnd) // Aligns the button to the top end (top right)
    ) {
      // The "three dots" icon button
      IconButton(onClick = { }) {
        Icon(
          imageVector = Icons.Default.Settings, // .MoreVert,
          contentDescription = "More options"
        )
      }
    }
  }

  @Composable
  fun PressableTextExample() {
    var isPressed by remember { mutableStateOf(false) }
    val text = remember { mutableStateOf("") }
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
                Toast("In box press")
              }
            }
            // onLongPress, onTap, onDoubleTap can also be used here
          )
        }
        .background(
          color = if (isPressed) Color.DarkGray else Color.LightGray,
          shape = MaterialTheme.shapes.small
        )
        .padding(6.dp),
      contentAlignment = Alignment.Center
    ) {

        TextField(

          value = text.value,
          onValueChange = { newText -> text.value = newText },
          label = { Text("Enter your name") },
          modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
              detectTapGestures(
                onLongPress = {
                  Toast("In Input press")
                }
                // onLongPress, onTap, onDoubleTap can also be used here
              )
            }
        )
//        Text("Hello, ${text.value}!")
//      Text(
//        text = if (isPressed) "Pressed!" else "Press me",
//        color = Color.White
//      )
    }
  }
}
