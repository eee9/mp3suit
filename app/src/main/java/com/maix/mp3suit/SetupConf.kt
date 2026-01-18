package com.maix.mp3suit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SetupConf: ViewModel() {
  val showDialog = mutableStateOf(false)
  val pathMp3 = mutableStateOf("Choose MP3 path...")
  val pathLrc = mutableStateOf("Choose LRC path...")
  val pathTxt = mutableStateOf("Choose TXT path...")
  val pathLog = mutableStateOf("Choose LOG path...")
  fun showOff() {
    showDialog.value = false
  }
  fun showOn() {
    showDialog.value = true
  }
}