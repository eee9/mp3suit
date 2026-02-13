package com.maix.mp3suit

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maix.mp3suit.ui.theme.Purple80

class ToolScreen(val main: MainActivity) {

//  val libTranslate = main.libTranslate

  @Composable
  fun ToolDialog() {
    var selectedLangOf = remember { mutableStateOf(main.libTranslate.langOf) }
    var selectedLangTo = remember { mutableStateOf(main.libTranslate.langTo) }
    Dialog(
      onDismissRequest = { main.showToolDialog.value = false },
      properties = DialogProperties(
        usePlatformDefaultWidth = false, // Crucial for full width
        decorFitsSystemWindows = false // Optional: allows drawing under system bars
      ),
    ) {
      Surface(
        modifier = Modifier
          .fillMaxWidth(0.70f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier
          .fillMaxHeight(fraction = 0.70f)
          .background(Purple80)
          .padding(6.dp)
        ) {
          Text("Tools Screen (uncompleted)", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(1.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
          ) {
            ChooseLang(selectedLangOf)
            ChooseLang(selectedLangTo)
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(1.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
          ) {
            ChooseFileButton(main)
            Text(
              main.chosenFileName?.value ?: "No file chosen",
              style = MaterialTheme.typography.titleLarge,
              fontSize = 14.sp,
              modifier = Modifier.padding(10.dp)
            )
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(1.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {

            Button(onClick = {
              if (selectedLangOf.value != main.libTranslate.langOf || selectedLangTo.value != main.libTranslate.langTo) {
                val msg = "Langs chosen: '${selectedLangOf.value}' -> '${selectedLangTo.value}'"
                main.Toast(msg)
                main.libTranslate.setupTranslator(selectedLangOf.value, selectedLangTo.value)
              }
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
  fun ChooseLang(lang: MutableState<String>) {
    // State to manage if the dropdown menu is expanded or not.
    var expanded by remember { mutableStateOf(false) }
    // State to hold the currently selected option.
    var selectedOption by remember { mutableStateOf(lang) }

      Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
        // Button to open the dropdown menu
        Button(onClick = { expanded = true }) {
          Text(text = selectedOption.value)
          Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
          )
        }

        // The actual dropdown menu
        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false }
        ) {
          main.libTranslate.mapLanguage.forEach { selectionOption_ ->
            DropdownMenuItem(
              text = { Text(selectionOption_.value) },
              onClick = {
                selectedOption.value = selectionOption_.value
                main.Toast("Selected: [${selectionOption_.value}]")
                expanded = false
                // Handle the selection, e.g., perform an action
              },
//              contentPadding = ExposedDropdownMenuDefaults.ItemPadding,
            )
          }
        }
      }

  }

  //==============================================================================================
  @Composable
  fun ChooseFileButton(main: MainActivity) {
    val filePickerLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.GetContent(),
      onResult = { uri: Uri? ->
        if (uri != null) {
          main.chosenFileName?.value = main.libFileURI.takeAbsolutePathFromUri(uri)
        }
      }
    )

    Column(modifier = Modifier.padding(16.dp)) {
      Button(onClick = { filePickerLauncher.launch("*/*") }) {
        Text("Choose file")
      }
    }
  }
}