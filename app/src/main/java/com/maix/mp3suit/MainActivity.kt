package com.maix.mp3suit

import android.os.Bundle
import android.os.Process
import android.provider.CalendarContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlin.system.exitProcess
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


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
    Text("Item 1 in Column")
    Spacer(modifier = Modifier.height(16.dp))
    Row(
      modifier = Modifier
        .background(Color.Yellow)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround // Distributes children evenly with space
    ) {
      Text("Item A in Row")
      Text("Item B in Row")
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


class MainActivity : ComponentActivity() {

  fun closeApp() {
    this.moveTaskToBack(true)
    Process.killProcess(Process.myPid())
    exitProcess(1)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()

    setContent {
      Mp3suitTheme {
        BasicLayoutExample()
      }
    }
  }
}


@Composable
fun mp3suit() {
  Mp3suitTheme {
    Column(
      modifier = Modifier
        .fillMaxSize() // Fills the maximum available space
//            .fillMaxWidth()
        .background(Color.Transparent)
        .padding(2.dp),
      verticalArrangement = Arrangement.Top, // Pushes children to the bottom
      horizontalAlignment = Alignment.Start // Centers the child horizontally
    ) {
      Header()
//          SimpleTextField()
      SimpleEditTextFieldExample("MP3", "")
      SimpleEditTextFieldExample("LRC", "")
//          SimpleEditTextFieldExample("TXT", "")
//          SimpleEditTextFieldExample("LOG", "")
      SimpleTextField()
      SimpleTextField()
//          ScrollableTextFieldScreen()
      TextFieldScreen()
//          ThreeDotsMenuExample()
      Footer()
    }

  }
}

@Composable
fun Example2() {
  Column(
    modifier = Modifier
      .fillMaxSize() // Fills the maximum available space
      .padding(16.dp),
    verticalArrangement = Arrangement.Bottom, // Pushes children to the bottom
    horizontalAlignment = Alignment.CenterHorizontally // Centers the child horizontally
  ) {
    Text(
      text = "Label",
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Start,
      color = Color.Cyan          )
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

@Composable
fun SimpleTextField() {
  // 1. Declare a mutable state to hold the user input.
  var text by remember { mutableStateOf("") } //

  // 2. Display the input field.

  TextField(
    modifier = Modifier
      .padding(4.dp)
      .fillMaxWidth(),
    value = text, // The current text to display.
    onValueChange = { newValue: String -> // Callback when the text changes.
      text = newValue // Update the state with the new value.
    },
    label = { Text("Enter your name") }, // Optional label/hint text.
    // other parameters like modifier, singleLine, etc.
  )
}

@Composable
fun Text2(msg: String) {
  Text(
    text = msg, fontSize = 16.sp,
  )

}

@Composable
fun SimpleEditTextFieldExample(msg: String, def: String) {
  // 1. Define the state for the text field
  var text by remember { mutableStateOf(def) }

  // 2. Use the OutlinedTextField composable
  OutlinedTextField(
    modifier = Modifier
      .padding(0.dp)
//        .height(55.dp)
      .fillMaxWidth(),

    singleLine = true,
//      fontSize = 12.sp,
    value = text, // The current value to display
    onValueChange = { newValue: String ->
      text = newValue // Update the state when the user changes the input
    },
    label = { Text2("$msg path:") }, // A label/hint

    // You can add other parameters here, e.g., modifier, keyboardOptions, etc.
  )
}

@Preview(showBackground = true)
@Composable
fun Header() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.LightGray),
//        .padding(16.dp),
//      horizontalArrangement = Arrangement.SpaceEvenly,
//      verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "mp3suit, ver 0.0.0, Q15 (mutable)",
//        modifier = Modifier
//        .size(40.dp),
//          .fillMaxWidth(),

      textAlign = TextAlign.Center,
      fontSize = 24.sp,
      fontFamily = FontFamily.SansSerif
//      color = Color.Cyan
    )
    ThreeDotsMenuExample()
  }
}

@Composable
fun Footer() {
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
//          .background(Color.LightGray)
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(onClick = { /* Click 1 */ }) {
        Text("Test...")
      }
//      Button(onClick = { closeApp() }) {
//        Text("Exit")
//      }
    }
  }
}

@Composable
fun TextFieldScreen() {
  // A large multiline TextField
  var largeText by remember { mutableStateOf("") }
  OutlinedTextField(
    value = largeText,
    onValueChange = { largeText = it },
    label = { Text("Large Input Field") },
    modifier = Modifier
      .fillMaxWidth()
//        .fillMaxHeight(),
      .height(200.dp), // Fixed height for a larger input area
    maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
  )
}

@Composable
fun ScrollableTextFieldScreen() {
  // 1. Remember the scroll state for the Column
  val scrollState = rememberScrollState()

  // 2. Use Column with verticalScroll modifier
  Column(
    modifier = Modifier
      .fillMaxSize()
      // Add IME padding to automatically handle the keyboard
//        .windowInsetsPadding(WindowInsets.imePadding())
      .verticalScroll(scrollState)
      .padding(16.dp)
  ) {
    // Add some introductory text and space
    Text("Please enter your details below:", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // Example TextFields
    for (i in 1..5) {
      var text by remember { mutableStateOf("") }
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Field $i") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    // A large multiline TextField
    var largeText by remember { mutableStateOf("") }
    OutlinedTextField(
      value = largeText,
      onValueChange = { largeText = it },
      label = { Text("Large Input Field") },
      modifier = Modifier
        .fillMaxWidth()
        .height(150.dp), // Fixed height for a larger input area
      maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ThreeDotsMenuExample() {
  // State to track if the dropdown menu is expanded
  var expanded by remember { mutableStateOf(false) }

  // Box to anchor the dropdown menu relative to the button
  Box(
    modifier = Modifier
//      .fillMaxSize()
      .wrapContentSize(Alignment.TopEnd) // Aligns the button to the top end (top right)
  ) {
    // The "three dots" icon button
    IconButton(onClick = { expanded = true }) {
      Icon(
        imageVector = Icons.Default.Settings, // .MoreVert,
        contentDescription = "More options"
      )
    }

    // The dropdown menu that appears when the button is clicked
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false } // Dismisses the menu when clicking outside
    ) {
      // Menu items
      DropdownMenuItem(
        text = { Text("Settings") },
        onClick = {
          // Handle item click
          expanded = false
        }
      )
      DropdownMenuItem(
        text = { Text("Profile") },
        onClick = {
          // Handle item click
          expanded = false
        }
      )
      DropdownMenuItem(
        text = { Text("Logout") },
        onClick = {
          // Handle item click
          expanded = false
        }
      )
    }
  }
}
