package com.maix.mp3suit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maix.mp3suit.ui.theme.Purple80

class ToolScreen {

  @Composable
  fun ToolDialog(main: MainActivity) {
    val mp3path = remember { mutableStateOf("_mp3path") }
    val mp3uri  = remember { mutableStateOf("_mp3uri") }
    Dialog(
      onDismissRequest = { main.showToolDialog.value = false },
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
//          .fillMaxHeight(fraction = 0.7f)
          .background(Purple80)
          .padding(6.dp)
        ) {
          Text("Tools Screen", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
          ChooseLang()
          ChooseLang()
//          main.setupScreen.ChoosePath(main,"MP3:", KEYMP3, mp3path, mp3uri)
          Text(
            main.chosenFileName?.value ?: "No file chosen",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp))
          FileChooserButtonExample(main)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(1.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {

            Button(onClick = {
              main.showToolDialog.value = false
            }) {
              Text(" OK ")
            }
          }
        }
      }
    }
  }

  @Composable
  fun FileChooserButtonExample(main: MainActivity) {
    // The Activity Result Launcher handles the system file picker interaction
    val filePickerLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.GetContent(),
      onResult = { uri: Uri? ->
        if (uri != null) {
          // Handle the selected file URI here
          main.Toast("File selected:\n[$uri]")
          main.chosenFileName?.value = main.libFileURI.takeAbsolutePathFromUri(uri)
        } else {
          main.Toast("No file selected")
        }
      }
    )

    Column(modifier = Modifier.padding(16.dp)) {
      Button(onClick = {
        filePickerLauncher.launch("*/*")
      }) {
        Text("Choose File")
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
//        enabled = false,
        onValueChange = {
          mExpanded = !mExpanded
//          mSelectedText = it
                        },
        modifier = Modifier
          .fillMaxWidth()
          .onGloballyPositioned { coordinates ->
            mTextFieldSize = coordinates.size.toSize()
          },
        label = {Text("Language of")},
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