package com.maix.mp3suit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat
import com.maix.mp3suit.ui.theme.Cyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme
import com.maix.mp3suit.ui.theme.Purple40
import org.intellij.lang.annotations.JdkConstants

class uilib {


  @Composable
  fun ScrollableTextFieldExample() {
    var text by remember { mutableStateOf("Initial long text that will eventually scroll vertically...") }

    // Add a very long initial text to demonstrate scrolling
    LaunchedEffect(Unit) {
      text = List(50) { index -> "Line $index: This is a very long line of text intended to force scrolling." }.joinToString(separator = "\\n")
    }

    // A container with a fixed height to make the internal content scrollable
    Column(modifier = Modifier.height(200.dp).padding(16.dp)) {
      TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
          .fillMaxWidth()
          // Apply the verticalScroll modifier
          .verticalScroll(rememberScrollState()),
        label = { androidx.compose.material3.Text("Enter long text here") }
      )
    }
  }

  @Composable
  fun Screen3() {
    Column(modifier = Modifier
      .fillMaxSize()
//      .background(Color.Yellow)
      .background(Cyan)
      .padding(3.dp)
    ) {
      Text(
        text = "mp3suit  (ver. 0.0.1, Q1B)",
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth(),
//          .background(Color.LightGray),
//          .padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif-smallcaps"),
            weight = FontWeight.Light
          )
        ),
        fontSize = 24.sp
      )
      var largeText by remember { mutableStateOf("...") }
      OutlinedTextField(
        value = largeText,
        onValueChange = { largeText = it },
        label = { Text("Logging", color = Color.Black) },
        modifier = Modifier
//          .background(Color.LightGray)
          .fillMaxWidth()
          .padding(1.dp)
          .weight(1f),
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Blue,   // Color when the field is focused
          unfocusedBorderColor = Color.Black, // Color when the field is not focused
          // You can also customize other colors here:
          // focusedLabelColor = Color.Green,
          // unfocusedLabelColor = Color.Red,
          // errorBorderColor = Color.Magenta
        )
      )
      Footer()
    }
  }

  @Composable
  fun Footer() {
    Column(
      modifier = Modifier
//        .fillMaxSize() // Fills the maximum available space
        .padding(2.dp),
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
        ButtonMx(onClick = { /* Click 1 */ }) {
          Text("Test...")
        }
        Button(onClick = { /* Click 1 */ }) {
          Text("Settings...")
        }
        Button(onClick = { /* closeApp() */}) {
          Text("Exit")
        }
      }
    }
  }

  @Composable
  fun ButtonMx(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Red, // Sets the background color
        contentColor = Color.White // Sets the text/content color
      ),
      content = content
    )
  }

  @Composable
  fun PressableTextExample() {
    var isPressed by remember { mutableStateOf(false) }

    Box(
      modifier = Modifier
        .pointerInput(Unit) {
          detectTapGestures(
            onPress = {
              isPressed = true
              try { awaitRelease() } finally { isPressed = false }
            }
            // onLongPress, onTap, onDoubleTap can also be used here
          )
        }
        .background(
          color = if (isPressed) Color.DarkGray else Color.LightGray,
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

  // Source - https://stackoverflow.com/a/69156877
// Posted by nglauber
// Retrieved 2026-01-11, License - CC BY-SA 4.0

  @Composable
  fun TestButton() {
    var isPressed by remember {
      mutableStateOf(false)
    }
    Column {
      Box(
        Modifier
          .pointerInput(Unit) {
            detectTapGestures(
              onPress = {
                try {
                  isPressed = true
                  // Start recording here
                  awaitRelease()
                } finally {
                  isPressed = false
                  // Stop recording here
                }
              },
            )
          }
          .background(
            MaterialTheme.colorScheme.primary.copy(alpha = if (isPressed) .88f else 1f),
            MaterialTheme.shapes.small
          )
          .padding(vertical = 8.dp, horizontal = 16.dp)
      ) {
        Text(
          text = "Press me!",
          Modifier.align(Alignment.Center),
          color = MaterialTheme.colorScheme.onPrimary
        )
      }
      Text(text = if (isPressed) "Pressed" else "Unpressed")
    }
  }

  @Composable
  fun Input() {
    var text by remember { mutableStateOf("") }

    TextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Enter Name") }
    )
  }

  @Composable
  fun InputPress() {
    var text by remember { mutableStateOf("") }

    TextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Enter Name") }
    )
  }
  @Composable
  fun InputUserExample() {
    val text = remember { mutableStateOf("") }

    Column {
      TextField(
        value = text.value,
        onValueChange = { newText -> text.value = newText },
        label = { Text("Enter your name") }
      )
      Text("Hello, ${text.value}!")
    }
  }

  @Composable
  fun CheckboxExample() {
    val checked = remember { mutableStateOf(false) }

    Row {
      Checkbox(
        checked = checked.value,
        onCheckedChange = { newChecked -> checked.value = newChecked }
      )
      Text("I accept the terms and conditions")
    }
  }

  @Composable
  fun Screen2() {
    Column(modifier = Modifier
      .fillMaxSize()
      .background(Color.Yellow)
      .padding(3.dp)
    ) {
      Text(
        text = "mp3suit",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif-smallcaps"),
            weight = FontWeight.Light
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "sans-serif-smallcaps",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif-smallcaps"),
            weight = FontWeight.Light
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Condensed Light",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif-condensed"),
            weight = FontWeight.Light
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Condensed Regular",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif-condensed"))),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Condensed Medium",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif-condensed"),
            weight = FontWeight.Medium
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Condensed Bold",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif"), weight = FontWeight.Bold)),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Thin",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif"), weight = FontWeight.Thin)),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Light",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif"),
            weight = FontWeight.Light
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Regular",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif"))),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Bold",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif"), weight = FontWeight.Bold)),
        fontSize = 24.sp
      )
      Text(
        text = "Roboto Black",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(
          Font(
            DeviceFontFamilyName("sans-serif"),
            weight = FontWeight.Black
          )
        ),
        fontSize = 24.sp
      )
      Text(
        text = "Noto Serif",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("serif"))),
        fontSize = 24.sp
      )
      Text(
        text = "Noto Serif Bold",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("serif"), weight = FontWeight.Bold)),
        fontSize = 24.sp
      )
      Text(
        text = "Droid Sans Mono",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("monospace"))),
        fontSize = 24.sp
      )
      Text(
        text = "Cutive Mono",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("serif-monospace"))),
        fontSize = 24.sp
      )
      Text(
        text = "Coming soon",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("casual"))),
        fontSize = 24.sp
      )
      Text(
        text = "Dancing Script",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("cursive"))),
        fontSize = 24.sp
      )
      Text(
        text = "Dancing Script Bold",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("cursive"), weight = FontWeight.Bold)),
        fontSize = 24.sp
      )
      Text(
        text = "Carrois Gothic SC",
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        fontFamily = FontFamily(Font(DeviceFontFamilyName("sans-serif-smallcaps"))),
        fontSize = 24.sp
      )
    }

  }

  @Composable
  fun Screen1() {
//    Box(modifier = Modifier
    Column(modifier = Modifier
      .fillMaxSize()
      .background(Color.Yellow)
      .padding(3.dp)
    ) {
      Header()
      var largeText by remember { mutableStateOf("") }
      OutlinedTextField(
        value = largeText,
        onValueChange = { largeText = it },
        label = { Text("Large Input Field") },
        modifier = Modifier
//          .background(Color.LightGray)
          .fillMaxWidth()
          .weight(1f),
        maxLines = Int.MAX_VALUE, // Allows the field itself to scroll internally
      )
      Footer()
    }
  }

  @Composable
  fun ColumnWithRemainingSpaceExample() {
    Column(
      modifier = Modifier
//        .fillMaxHeight()
        .fillMaxSize()
        .background(Color.Yellow)
        .padding(30.dp)
//        .height(50.dp) // Give the Row a fixed height for demonstration
    ) {
      Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(60.dp).background(Color.Magenta))

      // This Text fills all the horizontal space left between the Icons
      Text(
        text = "Text that fills remaining space",
        modifier = Modifier
          .background(Color.Blue)
          .fillMaxWidth()
          .weight(1f)
      )

      Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(40.dp))
    }
  }

  @Composable
  fun RowWithRemainingSpaceExample() {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp) // Give the Row a fixed height for demonstration
    ) {
      Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(40.dp))

      // This Text fills all the horizontal space left between the Icons
      Text(
        text = "Text that fills remaining space",
        modifier = Modifier.weight(1f)
      )

      Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(40.dp))
    }
  }


  @Composable
  fun FillRemainingSpaceExample() {
    Column(
      modifier = Modifier.fillMaxSize() // Make the Column fill the screen/parent
    ) {
      Text("Top Text")

      // This Spacer fills all the vertical space left after measuring the Texts
      Spacer(
        modifier = Modifier
          .background(Color.Magenta)
          .weight(1f)
      )

      Text("Bottom Text")
    }
  }

  @Composable
  fun Box() {
    Box(
      modifier = Modifier
        .background(Color.Yellow)
        .fillMaxSize()
    )
  }

  @Composable
  fun BoxVS() {
    val scrollState = rememberScrollState()
    val gradient =
      Brush.verticalGradient(
        listOf(Color.Red, Color.Blue, Color.Green),
        0.0f,
        10000.0f,
        TileMode.Repeated,
      )
    Box(
      Modifier.verticalScroll(scrollState)
        .fillMaxWidth()
        .requiredHeight(10000.dp)
        .background(brush = gradient)
    )
  }

  @Composable
  fun Columns() {
    Column(
      modifier = Modifier
        .fillMaxSize() // Fills the maximum available space
        .background(Color.Blue)
        .padding(20.dp),
//      verticalArrangement = Arrangement.Top, // Pushes children to the bottom
//      horizontalAlignment = Alignment.Start // Centers the child horizontally
    ) {
      TextMx("Line 1")
      TextMx("Line 2")
      TextMx("Line 3")
      Text(
        text = "Line last",
//        Modifier.verticalScroll(true)
      )
      Box(
        modifier = Modifier
//          .fillMaxSize() // Fills the maximum available space
          .background(Color.Magenta)
          .padding(40.dp),
//        contentAlignment = Alignment.Bottom as Alignment,
      ) {
        Text("In a box 2")
      }
      Box(
        modifier = Modifier
          .fillMaxSize() // Fills the maximum available space
          .background(Color.Green)
          .padding(10.dp),
      ) {
        Text("In a box 1")
      }
    }
  }

  // source: https://www.geeksforgeeks.org/kotlin/building-ui-using-jetpack-compose-in-android/
  data class BottomMenuContent (
    val title: String,
    @DrawableRes val iconId: Int,
    val iconId2: ImageVector
  )


  @Preview(widthDp = 700, heightDp = 1400)
  @Composable
  fun HomeScreen() {
    // this is the most outer box having all the views inside it
    Box(
      modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
    ) {
      Column {
        for(i in 1.. 30) TextMx("some text N $i")
        TextMx("text 001")
        TextMx("text 002")
        TextMx("text 003")
        TextMx("text 004")
        TextMx("text 005")
        TextMx("text 006")

      }
      BottomMenu(items = listOf(
        BottomMenuContent("Home1", R.drawable.ic_launcher_background, Icons.Default.Settings),
        BottomMenuContent("Home2", R.drawable.ic_launcher_background, Icons.Default.Settings),
        BottomMenuContent("Home3", R.drawable.ic_launcher_background, Icons.Default.Settings),
        BottomMenuContent("Home4", R.drawable.ic_launcher_background, Icons.Default.Settings),
      ), modifier = Modifier.align(Alignment.BottomCenter))
    }
  }

  @Composable
  fun U() {
    BottomMenu(items = listOf(
      BottomMenuContent("Home1", R.drawable.ic_launcher_background, Icons.Default.Settings),
      BottomMenuContent("Home2", R.drawable.ic_launcher_background, Icons.Default.Settings),
      BottomMenuContent("Home3", R.drawable.ic_launcher_background, Icons.Default.Settings),
      BottomMenuContent("Home4", R.drawable.ic_launcher_background, Icons.Default.Settings),
//      BottomMenuContent("Explore", 0, Icons.Default.Settings),
//      BottomMenuContent("Dark Mode", Icons.Default.Menu),
//      BottomMenuContent("Videos", Icons.Default.Build),
//      BottomMenuContent("Profile", Icons.Default.Settings),

//      BottomMenuContent("Home", Icons.Default.Settings),
//      BottomMenuContent("Explore", Icons.Default.Settings),
//      BottomMenuContent("Dark Mode", Icons.Default.Menu),
//      BottomMenuContent("Videos", Icons.Default.Build),
//      BottomMenuContent("Profile", Icons.Default.Settings),
    ))
//      , modifier = Modifier(Alignment.BottomCenter))
  }

  @Composable
  fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = Color.Green,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = Color.Blue,
    initialSelectedItemIndex: Int = 0
  ) {
    var selectedItemIndex by remember {
      mutableIntStateOf(initialSelectedItemIndex)
    }
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier
//        .fillMaxWidth()
        .background(Color.Blue)
        .padding(15.dp)

    ) {
      items.forEachIndexed { index, item ->
        BottomMenuItem(
          item = item,
          isSelected = index == selectedItemIndex,
          activeHighlightColor = activeHighlightColor,
          activeTextColor = activeTextColor,
          inactiveTextColor = inactiveTextColor
        ) {
          selectedItemIndex = index
        }
      }
    }
  }

  @Composable
  fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeHighlightColor: Color = Color.Green,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = Color.Blue,
    onItemClick: () -> Unit
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.clickable {
        onItemClick()
      }
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(if (isSelected) activeHighlightColor else Color.Transparent)
          .padding(10.dp)
      ) {
//        item.iconId2
        Icon(
          painter = painterResource(id = item.iconId),
          contentDescription = item.title,
          tint = if (isSelected) activeTextColor else inactiveTextColor,
          modifier = Modifier.size(20.dp)
        )
      }
      Text(
        text = item.title,
        color = if(isSelected) activeTextColor else inactiveTextColor
      )
    }
  }
  // source: https://www.geeksforgeeks.org/kotlin/building-ui-using-jetpack-compose-in-android/

  // ===============================================================================================
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

  @Composable
  fun ColumnExample() {
    Column(
      modifier = Modifier.padding(26.dp), // Add padding around the column
      verticalArrangement = Arrangement.spacedBy(18.dp) // Add space between items
    ) {
      Text(text = "Item 1")
      Text(text = "Item 2")
      Text(text = "Item 3")
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
        .height(50.dp)
        .background(Color.LightGray),
      text = " [ $text ] "
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

}