package com.maix.mp3suit

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.maix.mp3suit.MainActivity.Const.TAG
import com.maix.mp3suit.SetupScreen.Keys.MXPREF


class Translate(val main: MainActivity) {
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  companion object Keys {
    val LANG_OF = "lang_of"
    val LANG_TO = "lang_to"
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

  fun translateText(originalText: String) {
    commonTranslator
      .translate(originalText)
      .addOnSuccessListener { translatedText ->
        val msg1 = "$EOL[$langOfUpper]:$EOL$originalText$EOL"
        val translatedTextUpd = translatedText.replace("[", "$EOL[").trim()
        val msg2 = "$EOL[$langToUpper]:$EOL$translatedTextUpd$EOL"
//        Logd(msg1)
        Logd(msg2)
//        main.add2MainLog(msg1)
        main.add2MainLog(msg2)
      }
      .addOnFailureListener { exception ->
        Logd("Translate ERR: [$exception]")
      }
  }

}