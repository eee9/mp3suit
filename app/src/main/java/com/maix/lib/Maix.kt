package com.maix.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.os.Process
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
//import com.maix.mp3text.MainActivity.Companion.EOL
//import com.maix.mp3text.MainActivity.Companion.KEYLOG
//import com.maix.mp3text.MainActivity.Companion.KEYLRC
//import com.maix.mp3text.MainActivity.Companion.KEYMP3
//import com.maix.mp3text.MainActivity.Companion.LogNFlrc
//import com.maix.mp3text.MainActivity.Companion.LogNFmp3
//import com.maix.mp3text.MainActivity.Companion.Logdev
import com.maix.services.PlayedTrackReceiverConnection
import com.maix.services.PlayedTrackReceiverService
import java.text.DateFormat
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.regex.Pattern
import kotlin.system.exitProcess

class Maix {

  val TAG = "xMx lib.Maix"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  private fun Loge(msg: String) {
    Log.e(TAG, "ERROR: $msg")
  }

  // !!! TEMP
  fun Logdev(s: String) { Logd(s) }
  val TAB = "\t"
  val SLASH = "/"
  val EOL = "\n"


  // !!! TEMP
  val MXPREF  = "MXPREF2"
  val SUFFIX = "_URI"
  val KEYMP3  = "mp3path"
  val KEYLOG  = "logpath"
  val KEYTEXT = "textpath"
  val KEYLRC  = "lyricpath"





  private val libFileIO = FileIO()
  private val libZip = Zip()
//  private val mainLib = MainActivity

  fun ToastSh(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }

  fun closeApp(activity: Activity) {
    activity.moveTaskToBack(true)
    Process.killProcess(Process.myPid())
    exitProcess(1)
  }

  @SuppressLint("SimpleDateFormat")
  fun currTime(): String {
    return (SimpleDateFormat("HH:mm:ss")).format(Date())
  }

  fun currTimeExt(): String {
    return millisToDate(System.currentTimeMillis())
  }

  @SuppressLint("SimpleDateFormat")
  fun millisToDate(millis: Long): String {
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(millis)
    return formatter.format(calendar.getTime())
  }

  fun getApplicationName(context: Context): String {
    val applicationInfo = context.applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
      stringId
    )
  }

  fun dumpIntent(i: Intent): HashMap<String, String> {
    val res = hashMapOf<String, String>()
    val bundle = i.extras
    if (bundle != null) {
      val keys = bundle.keySet()
      val it = keys.iterator()
      Logd("\n>>>>\nDumping Intent start\n>>>>\n")
      while (it.hasNext()) {
        val key = it.next()
        val value: String = bundle.getString(key).toString()
        Logd("[" + key + "=" + bundle.getString(key) + "]")
        res[key] = value
      }
      Logd("\n<<<<\nDumping Intent end\n<<<<\n")
    } else {
      Logd("bundle is NULL")
    }
    return res;
  }

  //==============================================================================================
  private val messengerHandler: Messenger = Messenger(IncomingHandler())
  val connection = PlayedTrackReceiverConnection(messengerHandler)
  @SuppressLint("HandlerLeak")
  class IncomingHandler : Handler() {
    override fun handleMessage(msg: Message) {
      Log.d("xMx lib.Maix IncimingHendler", "handleMessage()")
      super.handleMessage(msg)
    }
  }

  fun bindService(activity: Activity) {
    activity.bindService(
      Intent(activity, PlayedTrackReceiverService::class.java),
      connection,
      BIND_AUTO_CREATE
    )
  }

//  fun doUnbindService(activity: Activity, connection1: PlayedTrackReceiverConnection) {
  fun doUnbindService(activity: Activity) {
    connection.messengerService
    try {
      val msg: Message = Message.obtain(null, PlayedTrackReceiverService.MSG_UNREGISTER_CLIENT)
      msg.replyTo = connection.handler
      connection.messengerService.send(msg)
      Logd("service send msg")
    } catch (e: RemoteException) {
      Logd("service unbind catch")
    }
    activity.unbindService(connection)
    Logd("service unbounded")
  }

  //==============================================================================================
  //==============================================================================================
  fun normalize(str: String): String {
    var res = str
    val regex1 = Regex(pattern = "\\(.+\\)")
    val regex2 = Regex(pattern = "\\[.+]")
    res = res.replace(regex1, "")
    res = res.replace(regex2, "")
    res = res.trim()
    return res
  }

  fun unfrench_(str: String): String {
    var res = str.lowercase()
//    val regex1 = Regex(pattern = "[]")
    res = res.replace(Regex(pattern = "[à]"), "a")
    res = res.replace("[éèê]", "e")
    res = res.replace("[î]", "i")
    res = res.replace("[ô]", "o")
    res = res.replace("[ùûü]", "u")
    res = res.replace("ç", "c")
    res = res.replace("œ", "oe")
    return res
  }

  fun deAccent(str: String): String {
    var res = ""
    if (str.isNotEmpty()) {
      val nfdNormalizedString: String = Normalizer.normalize(str, Normalizer.Form.NFD)
      val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
      res = pattern.matcher(nfdNormalizedString).replaceAll("")
    }
    return res
  }

  val MP3_PATHS = "mp3paths.txt"
  val MP3_PATHS_BAK = "mp3paths~.txt"
  fun findMP3file(context: Context, sharedPreferences: SharedPreferences, artist: String, track: String, album: String): String {
    var res = ""
    Logdev("Searching MP3 for A:'$artist', T:'$track', AL:'$album' ...")
    var msgToast = ""
    if (artist.isNotEmpty() && track.isNotEmpty()) {
      val pathLog = sharedPreferences.getString(KEYLOG, null) ?: "No path for LOG"
      val pathsFile = pathLog + SLASH + MP3_PATHS
      if (FileIO().isPathExist(pathsFile)) {
        var searchFor = "$artist - $track"
        var items: ArrayList<String> = findMP3line(pathsFile, artist, track)
        if (items.isEmpty()) {
          val artist_ = normalize(artist)
          val track_ = normalize(track)
          // Try to find changed items, del (), [], etc
          if ((artist_.compareTo(artist) != 0) || (track_.compareTo(track) != 0)) {
            Logdev("Searching MP3 for NORMALIZED A:'$artist_', T:'$track_' ...")
            items = findMP3line(pathsFile, artist_, track_)
          }
        }
        val pathMP3 = sharedPreferences.getString(KEYMP3, null) ?: ""
        if (items.size == 1) {
          if (pathMP3.isNotEmpty()) {
            res = pathMP3 + SLASH + items.last()
            if (libFileIO.isPathExist(res)) {
              msgToast = ""
            } else {
              msgToast = "!! NOT EXIST found .MP3 file '$res'"
              res = ""
            }
//          Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
          }
        }
        else if (items.size > 1) {
          res = pathMP3 + SLASH + items.first()
          Logdev("## MANY .MP3 for [$searchFor]:")
          var i = 1
          items.forEach {
            if (it.contains(album, true)) res = pathMP3 + SLASH + it // found .mp3 via album
            Logdev("$i.  ==> '$it'")
            i++
          }
          Toast.makeText(context, "# MANY [${items.size}] .MP3 for => '$searchFor'", Toast.LENGTH_LONG).show()

        } else { // size == 0
          msgToast = "-- NO .MP3 for [$searchFor]"
//          LogNFmp3(msgToast)
        }

      }
      else {
        msgToast = "Can't read MP3 paths in [$pathsFile]"
      }
      if (msgToast.isNotEmpty()) {
        Logdev(msgToast)
        Toast.makeText(context, msgToast, Toast.LENGTH_SHORT).show()
      }
      Logdev("Search of MP3 over. res => '$res'")
    } // if (artist.isNotEmpty() && track.isNotEmpty()) {
    return res
  }
  fun findMP3line(indexFile: String, artist: String, track: String): ArrayList<String> {
    val res = ArrayList<String>()
    val res1 = ArrayList<String>()
    // must be checked if exists before
    val data = libFileIO.readFileAsAL(indexFile)
    Logd("findMP3line. Lines in the file '$indexFile': ${data.size}")
    // search only track name in .mp3 files
    for (i in 0 until data.size) {
      if ((data[i].contains(track, true)) &&
          (data[i].endsWith(".mp3", true))) {
        res1.add(data[i])
      }
    }
    Logd("Step 1. Found MP3 items: [${res1.size}]")

    if (res1.isEmpty()) { // search for cases à la "tempête" != "tempete"
      val track_ = deAccent(track.lowercase())
      if (track != track_) Logd("Track was deAccent(): '$track' => '$track_'")
      for (i in 0 until data.size) {
        val line_ = deAccent(data[i].lowercase())
        if ((line_.contains(track_, true)) &&
          (line_.endsWith(".mp3", true))) {
          res1.add(data[i])
        }
      }
      Logd("Step 2. Found MP3 items: [${res1.size}]")
    }
    // try to find .mp3 with artist name
    if (res1.size > 1) {
      val artist_ = deAccent(artist.lowercase())
      if (artist != artist_) Logd("Artist was deAccent(): '$artist' => '$artist_'")
      for (j in 0 until res1.size) {
        if (res1[j].contains(artist, true)) res.add(res1[j])
        else {
          val found_ = deAccent(res1[j].lowercase())
          if (found_.contains(artist_, true)) res.add(res1[j])
        }
      }
      Logd("Step 9. Found MP3 items: [${res.size}]")
    }
    return res.ifEmpty { res1 }
  }

  //..............................................................................................
  fun rescanMP3(pathMp3: String, pathLog: String, context: Context) {
    var msgToast = "MP3 rescan starts..."
    ToastSh(msgToast, context)
    val dirList = libFileIO.readFolderAsAL(pathMp3, false).sorted()
    if (dirList.isNotEmpty()) {
      val mp3ListPath = pathLog + SLASH + MP3_PATHS
      val mp3ListBak  = pathLog + SLASH + MP3_PATHS_BAK
      libFileIO.renameFile(mp3ListPath, mp3ListBak)
      val dirListFiltered = dirList.filter {
        (!it.startsWith(".git") && !it.startsWith(".thumbnails"))
      }
      val dirContent = dirListFiltered.joinToString(EOL)
      libFileIO.writeString2File(mp3ListPath, dirContent, false)
    }
    msgToast = "MP3 rescan done."
    ToastSh(msgToast, context)
  }

  //==============================================================================================
  val INDEX_NAME = "0index.txt"
  fun findLRCfile(context: Context, sharedPreferences: SharedPreferences, searchFor: String, pathLRC: String): String {
    var res = ""
    Logdev("Searching LRC for [$searchFor]...")
    val pathLyric = sharedPreferences.getString(KEYLRC, null) ?: "No path for LYRIC"
//    Logd("& LYRIC: '$pathLyric'")
//    Toast.makeText(context, "LYRIC: [$pathLyric]", Toast.LENGTH_LONG).show()
    val indexFile = pathLyric + SLASH + INDEX_NAME
    var msgToast = ""
    if (FileIO().isPathExist(indexFile)) {
      var items: ArrayList<String> = findLRCline(indexFile, searchFor)
      if (items.isEmpty() && pathLRC.isNotEmpty()) {
        // No LRC found. Try use file path...
        val parts = pathLRC.split(SLASH)
        val searchFor2 = parts.last().replace(".lrc", "")
        if (searchFor2.isNotEmpty()) {
          Logdev("No LRC by tegs. Use info from path '$searchFor2' ...")
          items = findLRCline(indexFile, searchFor2)
        }
      }
      if (items.isNotEmpty()) {
        res = items.last()
        msgToast = "Found .LRC file '$res'"
//        Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
      }
      else {
        msgToast = "-- NO LRC -- '$searchFor'"
//        LogNFlrc(msgToast)
//        Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
      }
    }
    else {
      msgToast = "!!! LRC list READ ERROR [$indexFile]"
    }
    Logdev(msgToast)
    Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
    Logdev("Search of LRC over")
    return res
  }
  fun findLRCline(indexFile: String, searchFor: String): ArrayList<String> {
    val res = ArrayList<String>()
    // must be checked if exists before
    val data = libFileIO.readFileAsAL(indexFile)
    Logd("findLRCline. Lines in the file '$indexFile': ${data.size}")
    Logd("Searching  : '$searchFor'")
    for (i in 0 until data.size) {
      if (data[i].contains(searchFor, true)) res.add(data[i])
    }
    Logd("Found LRC items: [${res.size}]")
    if (res.size > 1) {
      for (i in 0 until res.size) Logdev("$i.  ==> '$res[$i]'")
    }
    return res
  }

  val archType = ".zip"
  fun copyLRCfile(context: Context, sharedPreferences: SharedPreferences, itemLRC: String, newLRCpath: String) {
    var msgToast = ""
//    val newLRCpath = mp3path.replace(".mp3", ".lrc")
    if (FileIO().isPathExist(newLRCpath)) {
      msgToast = "== LRC EXISTS =="
      Logdev(msgToast)
      Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
      return
    }
    // To do. Check if lrc file can be written
//    LogToast(context, "Copy '$itemLRC' => \n'$newLRCpath'")

    val parts: List<String> = itemLRC.split(SLASH)
    if (parts.size >= 2) {
      val pathLyric = sharedPreferences.getString(KEYLRC, null) ?: ""
      val foundArch = pathLyric + SLASH + parts[0] + archType
      val lrcFileName = parts.last()
      if (libFileIO.canReadPath(foundArch)) {
        Logd("Copying from [$foundArch] file '$lrcFileName'...")
        val ok = libZip.readFileInZip(foundArch, lrcFileName, newLRCpath)
        msgToast = if (ok) "++ LRC ADDED ++"
                   else "!! ERROR when unzip [$foundArch] ==> '$lrcFileName'"
      } else {
        msgToast = "!! CAN'T READ ZIP [$foundArch] !"
      }
    }
    Logdev(msgToast)
    Toast.makeText(context, msgToast, Toast.LENGTH_LONG).show()
  }
}