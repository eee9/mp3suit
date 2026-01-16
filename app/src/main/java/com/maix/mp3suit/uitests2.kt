package com.maix.mp3suit

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.core.net.toUri
import com.maix.lib.Maix
import com.maix.mp3suit.MainActivity.Companion.Logd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class uitests2 {

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
  val mainActivity = MainActivity()

  private fun openDirectory() {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
  }
//  val openDocumentTreeLauncher = mainActivity.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
  val openDocumentTreeLauncher = MainActivity().registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
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

  var _countFlow = MutableStateFlow(0)

  var countFlow: StateFlow<Int> = _countFlow

  fun increment() {
    _countFlow.value += 1
    countFlow = _countFlow
  }

  data class MainUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val count: Int = 22,
    val showDialog: Boolean = false,
  )

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

  fun updateName(newName: String) {
    _uiState.update { it.copy(name = newName) }
  }

  fun _inc_() {
    _uiState.update { it.copy(count = 3) }
  }

  @Composable
  fun CounterApp(viewModel: CounterViewModel) {
    // Access the state from the ViewModel
    val count by viewModel.count

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(text = "Count: $count")
      Button(onClick = { viewModel.incrementCount() }) {
        Text("Increment")
      }
    }
  }

  @Composable
  fun ShowDialog2(viewModel: CounterViewModel) {

    Column() {
      Button(onClick = { viewModel.showOn() }) {
        Text("Open Setup")
      }
      Spacer(modifier = Modifier.width(16.dp))
      Button(onClick = {
        val count = viewModel.count.intValue
        Toast("Counter is: [$count]")
      }) {
        Text("Show counter")
      }
      Spacer(modifier = Modifier.width(16.dp))
      Button(onClick = {
        val text = viewModel.name.value
        Toast("Text is: '$text'")
      }) {
        Text("Show text")
      }
      Spacer(modifier = Modifier.width(16.dp))
      Button(onClick = { libMaix.closeApp(MainActivity()) }) {
        Text("Exit")
      }
      if (viewModel.showDialog.value) {
        MainScreen(viewModel)
      }
    }
  }

  @Composable
  fun MainScreen(viewModel: CounterViewModel) {
    // Collect the single state from the ViewModel
    val name by viewModel.name
    val count by viewModel.count

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(text = "Count: $count")
      Button(onClick = { viewModel.incrementCount() }) {
        Text("Increment")
      }
      // Pass the state values and event callbacks to the stateless component
      NameEditor(
        name = name,
        onNameChange = { it ->
          viewModel.updateName(it)
        }
      )
      Button(onClick = { viewModel.showOff() }) {
        Text("Close Setup")
      }
    }
  }

  @Composable
  fun StatefulCounter() {
//    val viewModel: MainActivity = MainActivity()
//    val state by viewModel.uiState.collectAsState()
//    val count = state.count
//    Button(onClick = { viewModel._inc() }) {
//      Text("Clicked $count times")
//    }
  }

  @Composable
  fun SetupDialog(toShow: Boolean, count: Int, onClick: ()->Unit, toOff:  ()->Unit) {
    if (toShow) {
      Dialog(onDismissRequest = toOff) {
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
            StatelessCounter(count, onClick)
            Spacer(modifier = Modifier.height(36.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(onClick = toOff) {
                Text("Save and Close")
              }
              Spacer(modifier = Modifier.width(16.dp))
              Button(onClick = {
                libMaix.closeApp(MainActivity())
              }) {
                Text("Exit")
              }
            }
          }
        }
      }
    }
  }

  @Composable
  fun MainScreen0(toShow: Boolean, count: Int, onClick: ()->Unit, toOff:  ()->Unit) {

  }
  @Composable
  fun SetupWindowExample() {
//    var showSetupDialog by remember { mutableStateOf(false) }
    var showSetupDialog by rememberSaveable { mutableStateOf(true) }


    var _countFlow2 = MutableStateFlow(0)

    var countFlow2: StateFlow<Int> = _countFlow2

    fun increment2() {
      _countFlow2.value += 1
      countFlow2 = _countFlow2
    }

    val view = MainActivity()
//    val count by view.countFlow.collectAsState()

    Button(onClick = { showSetupDialog = true }) {
      Text("Open Setup")
    }

    var count3 by rememberSaveable { mutableStateOf(0) }
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
//            StatefulCounter()
//            StatelessCounter(count3, ::inc3)
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

  @Composable
  fun NameEditor(
    name: String,
    onNameChange: (String) -> Unit
  ) {
    TextField(
      value = name,
      onValueChange = onNameChange, // Event callback
      label = { Text("Enter your name") }
    )
  }

  @Composable
  fun StatelessCounter(count: Int, onClick : ()->Unit){
    Button(onClick = onClick) {
      Text("3: Clicked $count times")
    }
  }

}