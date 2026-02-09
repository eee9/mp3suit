package com.maix.mp3suit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maix.mp3suit.ui.theme.Purple80

class ToolScreen {

  @Composable
  fun ToolDialog(main: MainActivity, showToolDialog: MutableState<Boolean>) {
    Dialog(
      onDismissRequest = { showToolDialog.value = false },
      properties = DialogProperties(
        usePlatformDefaultWidth = false, // Crucial for full width
        decorFitsSystemWindows = false // Optional: allows drawing under system bars
      ),
    ) {
      Surface(
        modifier = Modifier
          .fillMaxWidth(0.93f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier
          .fillMaxHeight(fraction = 0.9f)
          .background(Purple80)
          .padding(6.dp)
        ) {
          Text("Tools Screen", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
          ChooseLang()
        }
      }
    }
  }

  @Composable
  fun ChooseLang() {
    var mExpanded by remember { mutableStateOf(false) }
    val mCities = listOf("English", "French", "Italian", "Ukrainian")

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
      Icons.Filled.KeyboardArrowUp
    else
      Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(1.dp)) {
      OutlinedTextField(
        value = mSelectedText,
        onValueChange = { mSelectedText = it },
        modifier = Modifier
          .fillMaxWidth()
          .onGloballyPositioned { coordinates ->
            mTextFieldSize = coordinates.size.toSize()
          },
        label = {Text("Language from")},
        trailingIcon = {
          Icon(icon,"contentDescription",
            Modifier.clickable { mExpanded = !mExpanded })
        }
      )

      DropdownMenu(
        expanded = mExpanded,
        onDismissRequest = { mExpanded = false },
        modifier = Modifier
          .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
      ) {
        mCities.forEach { label ->
          DropdownMenuItem(
            text = { Text(text = label) },
            onClick = {
              mSelectedText = label
              mExpanded = false
            }
          )
        }
      }
    }
  }
}