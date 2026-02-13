package com.maix.mp3suit

import android.util.Log

class Tests(val main: MainActivity) {

  val TAG = "xMx Tests"
  val EOL = "\n"
  val fileForChoose1 = "/storage/emulated/0/Pictures/88/79.txt"
  val fileForChoose2 = "/storage/emulated/0/Pictures/88/79.22.txt"
  val resultFile = "/storage/emulated/0/Pictures/88/res_test.lrc"

  fun logX(msg: String) { main.add2MainLog(msg) }
  fun Logd(msg: String) { Log.d(TAG, msg) }

  fun runTest() {
    val libFiles = main.libFileIO
    main.msgMainLog.value = ""
//    main.add2MainLog("Some tests run")
    val content1 = libFiles.readFile(fileForChoose1)
    val content2 = libFiles.readFile(fileForChoose2)
//    main.add2MainLog("Content1 len = [${content1.length}]")
//    main.add2MainLog("Content2 len = [${content2.length}]")
//    main.add2MainLog(content2)
    val arr1 = content1.split(EOL)
    val arr2 = content2.split(EOL)
//    main.add2MainLog("Lines 1 = [${arr1.size}]")
//    main.add2MainLog("Lines 2 = [${arr2.size}]\n")
//    listOfTimes(arr1)
    val mapTr = getTranslatedMap(arr2)
    val newLrc = createNewLrc(arr1, mapTr)
//    Logd(new_Lrc)
    main.add2MainLog(newLrc)
    libFiles.writeString2File(resultFile, newLrc, false)
  }

  val timeRx = Regex("(\\[[0-9].+])(.+)")
  fun getTranslatedMap(arr: List<String>): Map<String, String> {
    val res =  mutableMapOf<String, String>()
    var i = 0
    arr.forEach {
      i++
      val matchResult = timeRx.find(it)
      if (matchResult != null) {
        if (matchResult.groups.size > 2) {
          val time: String = matchResult.groups[1]?.value.toString()
          val text: String = matchResult.groups[2]?.value.toString()
          val time_upd = time.replace(" ", "")
//          Logd("$i: $time -> $time_upd. {$text}")
          res[time_upd] = text
        }
      }
    }
    return res
  }

  fun createNewLrc(arr: List<String>, trans: Map<String, String>): String {
    val res = mutableListOf<String>()
    var i = 0
    arr.forEach {
      i++
      if (it.contains("# http")) {
        res.add(it)
      } else {
        val matchResult = timeRx.find(it)
        if (matchResult != null) {
          if (matchResult.groups.size > 2) {
            val time: String = matchResult.groups[1]?.value.toString()
            val text: String = matchResult.groups[2]?.value.toString()
//          val time_upd = time.replace(" ", "")
            val text_tr: String = trans[time] ?: ""
//            Logd("$i: $time |  $text")
//            Logd("  :       |  $text_tr")
            val orig = "$time $text"
            val tranlate = "$time${text_tr.trim()}"
            res.add(orig)
            res.add(tranlate)
          }
        } else {
          res.add(it)
        }
      }
    }
    return res.joinToString(EOL)
  }
}