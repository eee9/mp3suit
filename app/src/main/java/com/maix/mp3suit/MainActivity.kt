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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.Cyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme

class MainActivity : ComponentActivity() {

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  val libMaix = Maix()
  lateinit var context: Context
  lateinit var dataView: CounterViewModel
  lateinit var setupConf: SetupConf

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
    setupConf = SetupConf()


    setContent {
      Mp3suitTheme {
        SetupDialog(setupConf)
      }
    }
  }

  @Composable
  fun SetupDialog(setupConf: SetupConf) {
    val u = uitests2()
    u.initContext(context)  // for Toast
    Column() {
      var _mp3path by remember { mutableStateOf(setupConf.pathMp3) }
      var _logpath by remember { mutableStateOf(setupConf.pathLog) }
      ChoosePath("MP3:", _mp3path)
      ChoosePath("LOG:", _logpath)
      CloseButton()
    }
  }

  @Composable
  fun CloseButton() {
    Button(onClick = { libMaix.closeApp(MainActivity()) }) {
      Text("Exit 80")
    }
  }

  @Composable
  fun ChoosePath(label: String, _text: MutableState<String>) {
    Button(
      onClick = {
        _text.value = "Choosing..."
        openDirectory(_text)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Text(text = label)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Text(_text.value, color = Cyan, modifier = Modifier.weight(1f),)
      Spacer(modifier = Modifier.padding(horizontal = 14.dp))
      Icon(
        imageVector = Icons.Default.Edit, // .LocationOn,
        contentDescription = "Edit"
      )
    }
  }

  lateinit var proxy: MutableState<String>
  fun openDirectory(_text:  MutableState<String>) {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
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

}
