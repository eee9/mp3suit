package com.maix.mp3suit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.maix.mp3suit.MainActivity.Companion.TAG


class Translate(main: MainActivity) {
  var main: MainActivity = main
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }

  fun getLanguage(lang: String): String {
    var res = ""
    when (lang.uppercase()) {
      "ENGLISH" -> res = TranslateLanguage.ENGLISH
      "FRENCH" -> res = TranslateLanguage.FRENCH
      "UKRAINIAN" -> res = TranslateLanguage.UKRAINIAN
      "RUSSIAN" -> res = TranslateLanguage.RUSSIAN
      "ITALIAN" -> res = TranslateLanguage.ITALIAN
      "SPANISH" -> res = TranslateLanguage.SPANISH
      "GERMAN" -> res = TranslateLanguage.GERMAN
    }
    return res
  }

  val langTo = TranslateLanguage.FRENCH
  private var options = TranslatorOptions.Builder()
    .setSourceLanguage(TranslateLanguage.ENGLISH)
    .setTargetLanguage(langTo)
    .build()

  private var commonTranslator = Translation.getClient(options)

  fun downloadModel() {
    val translator = Translation.getClient(options)
    val conditions = DownloadConditions.Builder().requireWifi().build()
    translator.downloadModelIfNeeded(conditions)
      .addOnSuccessListener {
        // Model downloaded successfully. You can now start translating.
        Logd("Model downloaded successfully")
        main.Toast("Model ready")
        val engText = "Here is some text for a check"
        main.Toast(engText)
        translateText(engText)
      }
      .addOnFailureListener { exception ->
        // Model couldn’t be downloaded or other internal error.
        Logd("Model download failed: $exception")
        main.Toast("Model download failed")
      }
   }

  fun translateText(englishText: String) {
    Logd("Test begins...")
    val textExample = "Just some part of the three books."
    Logd("EN  : '$englishText'")
    Logd("download...")
    commonTranslator.translate(englishText)
      .addOnSuccessListener { translatedText ->
        Logd("LANG2 : '$translatedText'")
        main.Toast("${langTo.uppercase()}: $translatedText")
      }
      .addOnFailureListener { exception ->
        Logd("ERR : $exception")
      }
      .addOnCanceledListener {
        Logd("addOnCanceledListener")
      }
      .addOnCompleteListener {
        Logd("addOnCompleteListener")
      }
    Logd("Test done.")
  }
}