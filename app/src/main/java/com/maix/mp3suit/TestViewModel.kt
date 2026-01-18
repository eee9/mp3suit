package com.maix.mp3suit

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
  // Mutable state inside ViewModel
  val count = mutableIntStateOf(0)
  val name = mutableStateOf("")
  val pathMp3 = mutableStateOf("Choose MP3 path...")
  val pathLrc = mutableStateOf("Choose LRC path...")
  val pathTxt = mutableStateOf("Choose TXT path...")
  val pathLog = mutableStateOf("Choose LOG path...")
  val showDialog = mutableStateOf(false)

  fun incrementCount() {
    count.intValue++
  }

  fun updateName(newName: String) {
    name.value = newName
  }

  fun newMp3(it: String) { name.value = it }

  fun showToggle() {
    showDialog.value != showDialog.value
  }
  fun showOff() {
    showDialog.value = false
  }
  fun showOn() {
    showDialog.value = true
  }

}