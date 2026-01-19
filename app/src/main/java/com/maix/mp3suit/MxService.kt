package com.maix.mp3suit

// MyBackgroundService.kt
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MxService : Service() {

  // Access the shared ViewModel instance
  private val viewModel = SharedViewModel.getInstance()

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    // ... perform your background work ...

    // Example: Update the LiveData with new data
    val newData = "@@ Data updated at ${System.currentTimeMillis()} @@"
    Log.d("xMx", "Sending data to UI: $newData")
    viewModel.updateData(newData) // Use postValue() for non-UI threads

    // ...
    return START_STICKY
  }

  override fun onBind(intent: Intent): IBinder? {
    // Not using bound service communication in this example
    return null
  }
}