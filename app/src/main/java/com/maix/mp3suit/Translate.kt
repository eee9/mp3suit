package com.maix.mp3suit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.maix.mp3suit.MainActivity.Companion.TAG


class Translate(val main: MainActivity) {
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  val EOL = "\n"

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

  val langOf = TranslateLanguage.ENGLISH
  val langOfUpper = langOf.uppercase()
  val langTo = TranslateLanguage.UKRAINIAN
  val langToUpper = langTo.uppercase()
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
        val msg = "Model [\"$langOfUpper\" -> \"$langToUpper\"] downloaded successfully"
        Logd(msg)
        main.Toast(msg)
//        val engText = "Here is some text for a check"
//        main.Toast(engText)
//        main.showSetupButton.value = true
        main.showTranslateButton.value = true
//        translateText(engText)
      }
      .addOnFailureListener { exception ->
        // Model couldn’t be downloaded or other internal error.
        Logd("Model download failed: $exception")
        main.Toast("Model download failed")
      }
   }

  fun translateText(originalText: String) {
    commonTranslator
      .translate(originalText)
      .addOnSuccessListener { translatedText ->
        val msg1 = "$EOL[$langOfUpper]:$EOL$originalText$EOL"
        val translatedTextUpd = translatedText.replace("[", "$EOL[").trim()
        val msg2 = "$EOL[$langToUpper]:$EOL$translatedTextUpd$EOL"
        Logd(msg1)
        Logd(msg2)
        main.add2MainLog(msg1)
        main.add2MainLog(msg2)
      }
      .addOnFailureListener { exception ->
        Logd("ERR : $exception")
      }
  }

}