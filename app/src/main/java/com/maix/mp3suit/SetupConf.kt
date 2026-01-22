package com.maix.mp3suit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SetupConf: ViewModel() {
  val EOL = "\n"

  val showDialog = mutableStateOf(false)
  val pathMp3 = mutableStateOf("Choose MP3 path...")
  val pathLrc = mutableStateOf("Choose LRC path...")
  val pathLog = mutableStateOf("Choose LOG path...")
  val pathTxt = mutableStateOf("Choose TXT path...")
  val uriMp3 = mutableStateOf("Choose MP3 uri...")
  val uriLrc = mutableStateOf("Choose LRC uri...")
  val uriLog = mutableStateOf("Choose LOG uri...")
  val uriTxt = mutableStateOf("Choose TXT uri...")
  var msgSetup = ""
  fun addSetup(t: String) { msgSetup += EOL + t}
//  val msgMain = mutableStateOf("LOG:\n")
  fun showOff() {
    showDialog.value = false
  }
  fun showOn() {
    showDialog.value = true
  }
}