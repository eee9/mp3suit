package com.maix.mp3suit

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.maix.mp3suit.ui.theme.Mp3suitTheme


class MainActivity : ComponentActivity() {

  lateinit var context: Context
  //  lateinit var context: Context
  fun Toast(msg: String) {
//      Toast
//        .makeText(context, msg, Toast.LENGTH_LONG)
//        .setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
//        .show()
//    if (context != null) {
//    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM or Gravity.END, 0, 0)
    toast.show()
//    } else {
//      Log.d("xMx", "Context is NULL!")
//    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
//    enableEdgeToEdge() // use all device screen

//    Toast("START 2...")
    val ui = Screen()
    context = this.applicationContext
    ui.initContext(context)  // for Toast

//    context = MainActivity().applicationContext
    setContent {
      Mp3suitTheme {
        ui.MainScreen()
//        ui.Footer()
      }
    }
  }
}
