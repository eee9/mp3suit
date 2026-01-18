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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Refresh
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.maix.mp3suit.ui.theme.Cyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme

class MainActivity : ComponentActivity() {

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  lateinit var context: Context
  lateinit var dataView: CounterViewModel
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
//    Toast("MainActivity onCreate")

    dataView = CounterViewModel()
//    val ui = Screen()
//    ui.initContext(context)  // for Toast
//    val t = uitests1()
//    t.initContext(context)  // for Toast

    setContent {
      Mp3suitTheme {
        Tests()
      }
    }
  }

  @Composable
  fun Tests() {
    val u = uitests2()
    u.initContext(context)  // for Toast
    Column() {
      ChoosePath("MP3:", dataView)
      //        ui.MainScreen()
//        t.ThreeDotsMenuExample()
//        t.ScrollableTextFieldScreen()
//        Input()
//        PressableText()
      u.ShowDialog2(dataView)
//        IconButton()
//        InputIcon()
//        ToggleIconButtonExample()
//        ButtonWithIconAndText() {
//          Toast("!!! pressed 77.")
//          openDirectory()
//        }

    }
  }

  @Composable
  fun ChoosePath(label: String, _dataView: CounterViewModel) {
    var _text by remember { mutableStateOf(dataView.pathMp3) }
//    val text by _dataView.pathMp3
    Button(
      onClick = {
//        _dataView.newMp3("New text value")
        _text.value = "LLLLL2"
        openDirectory(_text)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      // Place the Text composable

      Text(text = label)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
//      Input()
      Text(_text.value, color = Cyan, modifier = Modifier.weight(1f),)
      // Add a Spacer for space between the icon and the text
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      // Place the Icon composable
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = "Edit"
      )
    }
  }

  lateinit var proxy: MutableState<String>
  fun openDirectory(_text:  MutableState<String>) {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
    _text.value = "LLLLL 77"
    proxy = _text
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
      proxy.value = path
    }
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
      Text("some path is here", color = Cyan, modifier = Modifier.weight(1f),)
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

}
