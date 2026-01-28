package com.maix.mp3suit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

class MySecondActivityContract : ActivityResultContract<String, Int?>() {

  override fun createIntent(context: Context, input: String): Intent {
    Log.d("xMx4", "createIntent")
    return Intent(context, MySecondActivityContract::class.java)
      .putExtra("my_input_key", input)
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Int? = when {
    resultCode != Activity.RESULT_OK -> null
    else -> intent?.getIntExtra("my_result_key", 42)
  }

  override fun getSynchronousResult(context: Context, input: String): SynchronousResult<Int?>? {
    Log.d("xMx4", "getSynchronousResult")
    return if (input.isEmpty()) SynchronousResult(42) else null
  }
}