package com.maix.mp3suit

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
  // Mutable state inside ViewModel
  val count = mutableIntStateOf(0)
  val name = mutableStateOf("")
  val showDialog = mutableStateOf(false)

  fun incrementCount() {
    count.intValue++
  }

  fun updateName(newName: String) {
    name.value = newName
  }

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