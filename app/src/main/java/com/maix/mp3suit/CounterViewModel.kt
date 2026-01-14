package com.maix.mp3suit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
  // Mutable state inside ViewModel
  val count = mutableStateOf(0)
//  val count: StateFlow<Int> = _count as StateFlow<Int>

  val name = mutableStateOf("")
  val isLoading: Boolean = false
  val errorMessage: String? = null
  val showDialog = mutableStateOf(false)

  fun incrementCount() {
    count.value++
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