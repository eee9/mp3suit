package com.maix.mp3suit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.maix.mp3suit.MainActivity.Const.TAG
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

class Translate(val main: MainActivity) {
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  companion object Keys {
    val LANG_OF   = "lang_of"
    val LANG_TO   = "lang_to"
    val LAST_FILE = "last_file"
  }

  val EOL = "\n"

  val mapLanguage = hashMapOf(
    1 to "English"   ,
    2 to "French"    ,
    3 to "Ukrainian" ,
    4 to "Russian"   ,
    5 to "Italian"   ,
    6 to "Spanish"   ,
    7 to "German"    ,
  )

  fun getLang(lang: String): String {
    var res = ""
    when (lang.uppercase()) {
      "ENGLISH"   -> res = TranslateLanguage.ENGLISH
      "FRENCH"    -> res = TranslateLanguage.FRENCH
      "UKRAINIAN" -> res = TranslateLanguage.UKRAINIAN
      "RUSSIAN"   -> res = TranslateLanguage.RUSSIAN
      "ITALIAN"   -> res = TranslateLanguage.ITALIAN
      "SPANISH"   -> res = TranslateLanguage.SPANISH
      "GERMAN"    -> res = TranslateLanguage.GERMAN
    }
    return res
  }

  var langOf = "English"
  var langOfAbbr = getLang(langOf) // TranslateLanguage.ENGLISH
  var langOfUpper = langOfAbbr.uppercase()
  var langTo = "French"
  var langToAbbr = getLang(langTo)// TranslateLanguage.FRENCH
  var langToUpper = langToAbbr.uppercase()

  private var options = TranslatorOptions.Builder()
    .setSourceLanguage(langOfAbbr)
    .setTargetLanguage(langToAbbr)
    .build()
  private var commonTranslator = Translation.getClient(options)

  fun setupTranslator(lang1: String, lang2: String) {
    commonTranslator.close()
    langOf = lang1
    langTo = lang2
    langOfAbbr = getLang(langOf)
    langToAbbr = getLang(langTo)
    langOfUpper = langOfAbbr.uppercase()
    langToUpper = langToAbbr.uppercase()
    if (langToAbbr.isEmpty() || langOfAbbr.isEmpty()) return
    Logd("Translate: '$lang1' => '$lang2'")
    options = TranslatorOptions.Builder()
      .setSourceLanguage(langOfAbbr)
      .setTargetLanguage(langToAbbr)
      .build()
    commonTranslator = Translation.getClient(options)

    downloadModel()

  }

  fun downloadModel() {
    main.showTranslateButton.value = false
    val langs = "<\"$langOfUpper\" -> \"$langToUpper\">"
    Logd("Downloading model $langs...")
    val translator = Translation.getClient(options)
//    val conditions = DownloadConditions.Builder().requireCharging().requireWifi().build()
    val conditions = DownloadConditions.Builder().requireWifi().build()
    translator.downloadModelIfNeeded(conditions)
      .addOnSuccessListener {
        // Model downloaded successfully. You can now start translating.
        val msg = "Model $langs downloaded successfully"
        Logd(msg)
        main.Toast(msg)
        main.showTranslateButton.value = true
        main.SaveInShared(LANG_OF, langOf)
        main.SaveInShared(LANG_TO, langTo)
      }
      .addOnFailureListener { exception ->
        val msg = "Model download failed: [$exception]"
        Logd(msg)
        main.Toast(msg)
      }
   }

  val DELIM = "乌克兰 " // some wierd trick, 'cause translator removes EOLs in translated text
  fun translateText(originalText: String) {
    val t1 = originalText.replace("\n", DELIM)
    commonTranslator
      .translate(t1)
      .addOnSuccessListener { translatedText ->
        val msg1 = "$EOL[$langOfUpper]:$EOL$originalText$EOL"
        val msg1_ = "$EOL[1_]:$EOL$t1$EOL"
        val msg2_ = "$EOL[2_]:$EOL$translatedText$EOL"
//        val translatedTextUpd = translatedText.replace("[", "$EOL[").trim()
        val translatedTextUpd = translatedText.replace(DELIM, "\n").trim()
        val msg2 = "$EOL[$langToUpper]:$EOL$translatedTextUpd$EOL"
        Logd(msg1)
        Logd(msg1_)
        Logd(msg2_)
        Logd(msg2)
        val arrOrigin = originalText.split(EOL)
        val arrTranslated = translatedTextUpd.split(EOL)
//    main.add2MainLog("Lines 1 = [${arr1.size}]")
//    main.add2MainLog("Lines 2 = [${arr2.size}]\n")
//    listOfTimes(arr1)
        val mapTr = getTranslatedMap(arrTranslated)
        val newLrc = createNewLrc(arrOrigin, mapTr)
        main.msgMainLog.value = ""
        main.add2MainLog(newLrc)
        val newFileName = main.chosenFileName.value.replace(".lrc", ".4.lrc")
        main.libFileIO.writeString2File(newFileName, newLrc, false)
//        main.add2MainLog(msg1 + msg1_ + msg2_ + msg2)
//        main.add2MainLog(msg1)
//        main.add2MainLog(msg2)
      }
      .addOnFailureListener { exception ->
        Logd("Translate ERR: [$exception]")
      }
  }

  val timeRx = Regex("(\\[[0-9].+])(.+)")
  fun createNewLrc(arr: List<String>, trans: Map<String, String>): String {
    val res = mutableListOf<String>()
    arr.forEach {
      if (it.contains("# http")) { // for info in the end of files
        res.add(it)
      } else {
        val matchResult = timeRx.find(it)
        if (matchResult == null) {
          res.add(it)
        } else {
          if (matchResult.groups.size > 2) {
            val time: String = matchResult.groups[1]?.value.toString()
            val text: String = matchResult.groups[2]?.value.toString()
            if (text.trim().isEmpty()) res.add(time) // for empty line translate isn't need
            else {
              val textTr: String = trans[time] ?: ""
              res.add("$time $text")
              res.add("$time${textTr.trim()}")
            }
          } else {
            res.add(it)
          }
        }
      }
    }
    return res.joinToString(EOL)
  }
  fun getTranslatedMap(arr: List<String>): Map<String, String> {
    val res =  mutableMapOf<String, String>()
    arr.forEach {
      val matchResult = timeRx.find(it)
      if (matchResult != null) {
        if (matchResult.groups.size > 2) {
          val time: String = matchResult.groups[1]?.value.toString()
          val text: String = matchResult.groups[2]?.value.toString()
          val time_upd = time.replace(" ", "")
          res[time_upd] = text
        }
      }
    }
    return res
  }

  fun stringToDeferred(data: String): Deferred<String> {
    val deferred = CompletableDeferred<String>()
    // Immediately complete the deferred with the given string
    deferred.complete(data)
    return deferred
  }

  // Translate line by line, 'cause the translator is very ....
  suspend fun translateText2(originalText: String)  {
    val lines: List<String> = originalText.split(EOL)
//    val result: MutableList<String> = arrayListOf()
    val deferredResults = mutableListOf<Deferred<String>>()
    lines.forEach { line ->
      commonTranslator
        .translate(line)
        .addOnSuccessListener { translatedLine: String ->
//          main.add2MainLog(" 1: $line")
//          main.add2MainLog(" 2: $translatedLine")
          val dres: Deferred<String> = stringToDeferred(translatedLine)
          deferredResults.add(dres)
//          result.add(translatedLine)
        }
        .addOnFailureListener { exception ->
          Logd("Translate ERR: [$exception]")
        }
    }
    val results = deferredResults.awaitAll()
    val translatedText = results.joinToString()
    main.add2MainLog("ORIGINAL:$EOL$originalText")
    main.add2MainLog("TRANSLATE:$EOL$translatedText")
  }

}