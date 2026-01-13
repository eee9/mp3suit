package com.maix.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process

import android.util.Log
import android.widget.Toast
import java.text.DateFormat
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.regex.Pattern
import kotlin.system.exitProcess

class Maix {

  val TAG = "xMx lib.Maix"
  val TAB = "\t"
  val SLASH = "/"

  private fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  private fun Loge(msg: String) {
    Log.e(TAG, "ERROR: $msg")
  }

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
}