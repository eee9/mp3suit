package com.maix.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.maix.lib.FileIO
import com.maix.lib.FileURI
import com.maix.lib.Maix
//import com.maix.mp3text.MainActivity.Companion.Logdev

class PlayedTrackReceiver() : BroadcastReceiver() {
  companion object {
    const val TAG = "xMx2 RECEIVER"
  }
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  fun Loge(msg: String) {
    Log.e(TAG, msg)
  }
  fun ToastS(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }
  fun ToastL(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
  }

  val libFileIO = FileIO()
  val libFileURI = FileURI()
  val libMaix = Maix()

  val filter: IntentFilter
    get() {
      val iF = IntentFilter()
      iF.addAction("com.amazon.mp3.metachanged")
      iF.addAction("com.andrew.apollo.metachanged")
      iF.addAction("com.android.music.metachanged")
      iF.addAction("com.android.music.musicservicecommand")
      iF.addAction("com.android.music.playbackcomplete")
      iF.addAction("com.android.music.playstatechanged")
      iF.addAction("com.android.music.queuechanged")
      iF.addAction("com.android.music.updateprogress")
      iF.addAction("com.htc.music.metachanged")
      iF.addAction("com.miui.player.metachanged")
      iF.addAction("com.nomad88.nomadmusic.metachanged")
      iF.addAction("com.nullsoft.winamp.metachanged")
      iF.addAction("com.rdio.android.metachanged")
      iF.addAction("com.real.IMP.metachanged")
      iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged")
      iF.addAction("com.sec.android.app.music.metachanged")
      iF.addAction("com.sonyericsson.music.metachanged")
      iF.addAction("fm.last.android.metachanged")
      iF.addAction("org.lineageos.eleven.metachanged")
      return iF
    }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onReceive(context: Context, intent: Intent) {
    try {
      Logd("onReceive starts")
      val extrasMap = libMaix.dumpIntent(intent)
      val action = intent.action
      val cmd = intent.getStringExtra("command")
      val artist: String = intent.getStringExtra("artist") ?: ""
      val album: String = intent.getStringExtra("album") ?: ""
      val track: String = intent.getStringExtra("track") ?: ""
      val id: String = extrasMap.get("id") ?: "0"
      val idNumb: Long = if (id != "null") id.toLong() else 0
//      val externalAudioUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//      val pathExt = libFileURI.getRealPathFromURI(context, externalAudioUri)
      val specificAudioUri: Uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
      var idMsg = "[NO ID]"
      var pathSp: String = ""
      if (idNumb > 0) {
        idMsg = id
        pathSp = libFileURI.getRealPathFromURI(context, specificAudioUri)
      }
      Logd("Received $cmd : $action {ID=$idMsg, dur=${extrasMap["duration"]}")
      Logd("$artist : $album : $track | $this.isPlaying")
      Logd("URI sp. : $specificAudioUri")
      val path = specificAudioUri.path
      Logd("PATH      : $path")
      Logd("PATH Sp   : $pathSp")

      val checkSp = libFileIO.msgPathRights(pathSp)
      Logd("Access PATH Sp check: [$checkSp]")

      val searchFor = "$artist - $track"
      val msg = "A: '$artist', T: '$track', AL: '$album', ID: $idMsg\n ==> [$pathSp]"
//      Logdev(msg)
      if (runJustOne && track.trim().isNotEmpty() && artist.trim().isNotEmpty()) {
//        runJustOne = false
        var doLRC = true
        var pathLRC = ""
        if (pathSp.isEmpty()) {
          pathSp = libMaix.findMP3file(context, PlayedTrackReceiverService.sharedPreferences, artist.trim(), track.trim(), album.trim())
        }
        if (pathSp.isNotEmpty()) {
          pathLRC = pathSp.replace(".mp3", ".lrc")
          doLRC = !FileIO().isPathExist(pathLRC)
        }
        if (doLRC) { // do if no LRC file already
          val lrcLine = libMaix.findLRCfile(context, PlayedTrackReceiverService.sharedPreferences, searchFor, pathLRC)
          Logd("SERV. Before LRC copy:")
          Logd("      lrc: '$lrcLine'")
          Logd("      mp3: '$pathSp'")
          if (lrcLine.isNotEmpty()) {
            if (FileIO().isPathExist(pathSp)) {
              libMaix.copyLRCfile(
                context,
                PlayedTrackReceiverService.sharedPreferences,
                lrcLine,
                pathLRC
              )
            }
          } else {
            Logd("SERV. No LRC copy.")
          }
        } else {
//          Logdev("Service. LRC exists '$pathLRC'.")
          ToastS(context, "== SERV LRC EXISTS ==")
        }
      }

    } catch (e: Exception) {
      Loge("xMx onReceive ERR: $e")
    }
    Logd("onReceive ends")
  }
  private var runJustOne = true
}
