package com.maix.mp3suit

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.maix.lib.Maix
import com.maix.mp3suit.ui.theme.Cyan
import com.maix.mp3suit.ui.theme.Mp3suitTheme

class MainActivity: ComponentActivity() {

  companion object {
    const val TAG = "xMx3"
    fun Logd(msg: String) {
      Log.d(TAG, msg)
    }
  }

  val libMaix = Maix()
  lateinit var context: Context
  lateinit var dataView: TestViewModel

  fun Toast(msg: String) {
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
  }

  // !!! Turn OFF the service here for debug
  private val runSERVICE = true
  //  private val runSERVICE = false

  val MXPREF  = "MXPREF2"
  val SUFFIX = "_URI"
  val KEYMP3  = "mp3path"
  val KEYLOG  = "logpath"
  val KEYTEXT = "textpath"
  val KEYLRC  = "lyricpath"

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen
    context = this.applicationContext
    Toast("MainActivity onCreate")

    if (runSERVICE) {
      libMaix.bindService(this)
      Logd("after bindService")
    }

//    dataView = TestViewModel()
    val mainScreen = MainScreen()
    mainScreen.initContext(context)  // for Toast

    val setupConf = SetupConf()

    setContent {
      Mp3suitTheme {
//        mainScreen.ShowMainScreen()
        SetupDialog(setupConf)
      }
    }
  }

  // for listen service
  override fun onDestroy() {
    super.onDestroy()
    Logd("Main OnDestroy")
    if (runSERVICE) libMaix.doUnbindService(this)
  }

  @Composable
  fun CloseButton() {
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .padding(20.dp),
      onClick = { libMaix.closeApp(MainActivity()) }

    ) {
      Text("Exit /Q1J.02")
    }
  }

  @Composable
  fun SetupDialog(setupConf: SetupConf) {
    val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
//    val pathMp3 = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
//    val pathText = sharedPreferences.getString(KEYTEXT, null) ?: "No path for TEXT found."
//    val pathLyric = sharedPreferences.getString(KEYLRC, null) ?: "No path for LYRIC found."
//    val pathLog = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    var _mp3path by remember { mutableStateOf(setupConf.pathMp3) }
    var _lrcpath by remember { mutableStateOf(setupConf.pathLrc) }
    var _logpath by remember { mutableStateOf(setupConf.pathLog) }
    _mp3path.value = sharedPreferences.getString(KEYMP3, null) ?: "No MP3 path found."
    _lrcpath.value = sharedPreferences.getString(KEYLRC, null) ?: "No LRC path found."
    _logpath.value = sharedPreferences.getString(KEYLOG, null) ?: "No LOG path found."
    Column(modifier = Modifier.padding(10.dp)) {
      ChoosePath("MP3:", KEYMP3, _mp3path)
      ChoosePath("LRC:", KEYLRC, _lrcpath)
      ChoosePath("LOG:", KEYLOG, _logpath)
      CloseButton()
    }
  }
  @Composable
  fun ChoosePath(label: String, _key: String, _text: MutableState<String>) {
    Button(
      onClick = {
        _text.value = "Choosing..."
        openDirectory(_key, _text)
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
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

  lateinit var proxyState: MutableState<String>
  var proxyKey: String = ""
  fun openDirectory(_key: String, _text:  MutableState<String>) {
    val initialUri: Uri = "".toUri()
    openDocumentTreeLauncher.launch(initialUri)
    proxyState = _text
    proxyKey = _key
  }
  val openDocumentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
    if (uri != null) {
      val path: String = uri.path ?: "NO PATH"
      Logd("URI  : '$uri'")
      val decode: String = Uri.decode(uri.path)
      Logd("### Dec. : '$decode'")
      val lastPath = uri.lastPathSegment
      Logd("URI lastPath : '$lastPath'")
      Logd("Path  :  '$path'")
      Logd("... saving done.")
      if (path.isNotEmpty()) {
        proxyState.value = path
        if (proxyKey.isNotEmpty()) {
          val sharedPreferences: SharedPreferences = getSharedPreferences(MXPREF, MODE_PRIVATE)
          sharedPreferences.edit { putString(proxyKey, path) }
        }
      }
    }
  }

}
