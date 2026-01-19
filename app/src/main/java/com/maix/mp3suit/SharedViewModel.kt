package com.maix.mp3suit

// SharedViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

  // MutableLiveData to be updated by the service (or repository)
  private val _serviceData = MutableLiveData<String>()
  val serviceData: LiveData<String> = _serviceData

  // Function to update the data, called from a background thread
  fun updateData(data: String) {
    // Use postValue() because the service might be calling from a non-UI thread
    Log.d("xMx", "Receiving data in UI: '$data'")
    _serviceData.postValue(data)
  }

  // A static-like instance if you need a simple singleton approach for the service
  // For more complex apps, use dependency injection (Dagger/Hilt)
  companion object {
    private var INSTANCE: SharedViewModel? = null
    fun getInstance(): SharedViewModel {
      if (INSTANCE == null) {
        INSTANCE = SharedViewModel()
      }
      return INSTANCE!!
    }
  }
}