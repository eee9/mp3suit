package com.maix.services

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log

class PlayedTrackReceiverConnection(@JvmField val handler: Messenger) : ServiceConnection {
  companion object {
    private const val TAG = "xMx2 CONNECTION"
  }
  private var messenger: Messenger? = null

  override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
    messenger = Messenger(service)
    try {
      Log.d(TAG, "onServiceConnected")
      val msg = Message.obtain(null, PlayedTrackReceiverService.MSG_REGISTER_CLIENT)
      msg.replyTo = handler
      messenger!!.send(msg)
    } catch (e: RemoteException) {
      // In this case the service has crashed before we could even do anything with it
      Log.d(TAG, "onServiceConnected error: $e")
    }
  }

  override fun onServiceDisconnected(className: ComponentName?) {
    // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
    messenger = handler
    Log.d(TAG, "onServiceDisconnected")
  }

  val messengerService: Messenger
    get() = messenger!!

}
