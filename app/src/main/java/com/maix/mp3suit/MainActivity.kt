package com.maix.mp3suit

import android.app.Activity
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit
import androidx.core.net.toUri
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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
    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
    toast.show()
  }

  var _countFlow = MutableStateFlow(0)

  var countFlow: StateFlow<Int> = _countFlow

  fun increment() {
    _countFlow.value += 1
    countFlow = _countFlow
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
        SetupWindowExample()
//        FlowScreen()
      }
    }
  }

  @Composable
  fun FlowScreen(view: MainActivity, count: Int) {
    Button(onClick = { view.increment() }) {
      Text("Count 1: $count")
    }
  }

  @Composable
  fun FlowScreen2(count: Int, onClick : ()->Unit) {
    Button(onClick = onClick) {
      Text("Count 2: $count")
    }
  }

  @Composable
  fun SetupWindowExample() {
    var showSetupDialog by remember { mutableStateOf(false) }
    var showSetupDialog2 by rememberSaveable { mutableStateOf(false) }


    var _countFlow2 = MutableStateFlow(0)

    var countFlow2: StateFlow<Int> = _countFlow2

    fun increment2() {
      _countFlow2.value += 1
      countFlow2 = _countFlow2
    }

    val view = MainActivity()
    val count by view.countFlow.collectAsState()

    Button(onClick = { showSetupDialog = true }) {
      Text("Open Setup")
    }

    var count3 by remember { mutableStateOf(0) }
    fun inc3() { count3++ }
    if (showSetupDialog) {
      Dialog(onDismissRequest = { showSetupDialog = false }) {
        // Content of your setup window goes here
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = MaterialTheme.colorScheme.surface,
          contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Setup Configuration", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Add your settings UI elements (e.g., Checkbox, TextField)
            var setting1Enabled by remember { mutableStateOf(true) }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(checked = setting1Enabled, onCheckedChange = { setting1Enabled = it })
              Text("Enable Feature X")
            }
            FlowScreen(view, count)
            FlowScreen2(_countFlow2.value, ::increment2)
            StatefulCounter()
            StatelessCounter(count3, ::inc3)
            Spacer(modifier = Modifier.height(36.dp))
            Button(onClick = {
              // Handle saving settings
              showSetupDialog = false
            }) {
              Text("Save and Close")
            }
          }
        }
      }
    }
  }

  @Composable
  fun StatefulCounter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
      Text("Clicked $count times")
    }
  }

  @Composable
  fun StatelessCounter(count: Int, onClick : ()->Unit){
    Button(onClick = onClick) {
      Text("3 Clicked $count times")
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
