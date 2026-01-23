package com.maix.mp3suit

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.MxCyan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class uitests2 {

  lateinit var text: MutableState<String>
  @Composable
  fun SimpleTextFieldExample() {
    // 1. Define and remember the state of the text field
    text = remember { mutableStateOf("") } // The initial value is an empty string

    Column(modifier = Modifier.padding(16.dp)) {
      // 3. Display the current input in another Text composable
      Text(text = "You typed: ${text.value}", modifier = Modifier.padding(top = 16.dp))
      // 2. The TextField composable
      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        // By default, singleLine is false, making it multi-line
        singleLine = false,
        // Optional: set a maximum number of lines
        maxLines = 5,
        value = text.value, // The current text to display
        onValueChange = { newText ->
          text.value = newText // Update the state whenever the user types
        },
        label = { Text("Enter your name") } // Optional placeholder label
      )
      OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text ("Label") },
        minLines = 3,
        maxLines = 3,
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)
      )
      Footer5()
    }
  }

  @Composable
  fun Footer5() {
    Button( onClick = { text.value = "(7171)" } ) {
      Text("Check...")
    }
    Button( onClick = { text.value += " +3939 " } ) {
      Text("Add...")
    }
    Button( onClick = { Toast(text.value) } ) {
      Text("Show...")
    }
  }

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
  fun ButtonWithIconAndText(onClick: () -> Unit) {
    Button(
      onClick = onClick,
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      // Place the Text composable

      Text(text = "LOG:")
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
//      Input()
      Text("some path is here", color = MxCyan, modifier = Modifier.weight(1f),)
      // Add a Spacer for space between the icon and the text
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      // Place the Icon composable
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = "Edit"
      )
    }
  }

  @Composable
  fun Input() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Input field") },
//      modifier = Modifier.fillMaxWidth()
    )
  }

  @Composable
  fun ToggleIconButtonExample() {
    IconButton(onClick = {  }) {
      Icon(
        imageVector = Icons.Default.Settings, // .MoreVert,
        contentDescription = "More options"
      )
    }
  }
  @Composable
  fun InputIcon() {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
      value = "jlkjllkjl",
      onValueChange = {
//        onValueChange(it)
        Toast("InputIcon pressed.")
      },
      visualTransformation = if (showPassword) {
        VisualTransformation.None
      } else {
        PasswordVisualTransformation()
      }, trailingIcon = {
        IconButton(onClick = {}) {VectorIcon(imageVector = Icons.Filled.Refresh)}
//        if (showPassword) {
//          IconButton(onClick = { showPassword = false }) {
//            VectorIcon(imageVector = Icons.Filled.Visibility)
//          }
//        } else {
//          IconButton(onClick = { showPassword = true }) {
//            VectorIcon(imageVector = Icons.Filled.VisibilityOff)
//          }
//        }
      }
    )
  }

  @Composable
  fun VectorIcon(imageVector: ImageVector) {
    TODO("Not yet implemented")
  }

  @Composable
  fun IconButton() {
    PressIconButton(
      onClick = {},
      icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
      text = { Text("Add to cart") }
    )
  }

  @Composable
  fun PressIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null
  ) {
    val isPressed = interactionSource?.collectIsPressedAsState()?.value ?: false

    Button(
      onClick = onClick,
      modifier = modifier,
      interactionSource = interactionSource
    ) {
      AnimatedVisibility(visible = isPressed) {
        if (isPressed) {
          Row {
            icon()
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
          }
        }
      }
      text()
    }
  }

  @Composable
  fun PressableText() {
    var isPressed by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
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
      Column() {
        Text(
          text = if (isPressed) "Pressed!" else "Press me",
          color = Color.White
        )
        OutlinedTextField(
          value = text,
//          enabled = false,
          onValueChange = { Toast("Input pressed 2.") },

          label = { Text("Input field") },
          modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
              detectTapGestures(
                onPress = {
                  Toast("Input pressed 1.")
                }
                // onLongPress, onTap, onDoubleTap can also be used here
              )
            }
        )
      }
    }
  }

  @Composable
  fun CounterApp(viewModel: TestViewModel) {
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
  fun ShowDialog2(viewModel: TestViewModel) {

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
  fun MainScreen(viewModel: TestViewModel) {
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