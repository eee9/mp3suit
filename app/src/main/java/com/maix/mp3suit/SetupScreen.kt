package com.maix.mp3suit

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.maix.mp3suit.ui.theme.MxGreen

class SetupScreen: ComponentActivity() {
  val TAG = "xMx5"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  @Composable
  fun Setup77(main: MainActivity) {
    Column(
      modifier = Modifier
        .background(MxGreen)
        .padding(8.dp)
    ) {
      Button( onClick = {
        Logd("@@ opening 2...")
        openDirectory(main)
      } ) {
        Text("@@ Open...")
      }
    }
  }

  //==============================================================================================
  // Initialize the ActivityResultLauncher
  fun openDirectory(main: MainActivity) {
    val initialUri = "".toUri()
    main.openDocumentTreeLauncher.launch(initialUri)
  }



}