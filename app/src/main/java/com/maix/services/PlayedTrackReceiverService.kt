package com.maix.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.maix.mp3suit.MainActivity

class PlayedTrackReceiverService : Service(), PlayedTrackReceiverInterface {
  companion object {
    private const val TAG = "xMx2 SERVICE"

    const val MSG_REGISTER_CLIENT: Int = 1
    const val MSG_UNREGISTER_CLIENT: Int = 2
//    var sharedPreferences: SharedPreferences? = null
  }
  fun mLogd(msg: String) {
    Log.d(TAG, msg)
  }
  val mMessenger: Messenger =
    Messenger(IncomingHandler()) // Target we publish for clients to send messages to IncomingHandler.
  private var receiver: PlayedTrackReceiver? = null


  override fun onBind(intent: Intent?): IBinder? {
    return mMessenger.binder
  }
  
  override fun onCreate() {
    super.onCreate()
    receiver = PlayedTrackReceiver()
    registerReceiver(receiver, receiver!!.filter)
    mLogd("Service Started.")
    val mainActivity = MainActivity()
    val sharedPreferences = getSharedPreferences(mainActivity.MXPREF, MODE_PRIVATE)
    val pathMp3 = sharedPreferences.getString(mainActivity.KEYMP3, null) ?: "~~ No MP3 path found."
    mLogd("~~ MP3 path: '$pathMp3'")
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    mLogd("Received start id $startId: $intent")
    return START_STICKY // run until explicitly stopped.
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(receiver)
    mLogd("Service Stopped.")
  }

  internal class IncomingHandler : Handler() {
    // Handler of incoming messages from clients.
    override fun handleMessage(msg: Message) {
      super.handleMessage(msg)
    }
  }

}
