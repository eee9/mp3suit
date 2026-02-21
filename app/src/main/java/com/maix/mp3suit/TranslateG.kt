package com.maix.mp3suit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TranslateG : ComponentActivity() {
  
  val TAG = "xMx3"
  fun Logd(msg: String) {
    Log.d(TAG, msg)
  }
  
  val EOL = "\n"
  val langOf = "en"
  val langTo = "fr"
//  val langTo = "fr"
//  val langTo = "it"
  val MARKB = "##"
  val MARKE = "##"
  val MARKMAP = mapOf(
    "en" to "wall",
    "fr" to "mur",
    "uk" to "стіна",
    "it" to "muro",
  )
  var MARKWORDOF: String = MARKMAP[langOf] ?: "wall"
  var MARKWORDTO: String = MARKMAP[langTo] ?: "mur"

  val textOrigin = "Some spark of text."
//  val textOrigin = """
//[01:04.88]Devant moi nébuleuses obscures
//[01:10.83]Moi diffuse, j'ai fait ce que j'ai pu
//"""
  var textTranslated: String = ""
  var pageFinished = false
  
//  val url = "https://example.com/"
//    val url = "https://www.wufoo.com/gallery/templates/search/?s={ORIGIN}"
    val url = "https://translate.google.com/?sl={LANGOF}&tl={LANGTO}&text={ORIGIN}&op=translate"
  
  fun encodeText(text: String): String {
    return URLEncoder.encode(text, StandardCharsets.UTF_8.name())
  }
  fun decodeText(text: String): String {
    return URLDecoder.decode(text, StandardCharsets.UTF_8.name())
  }
  
  fun findTextByMarks(rawText: String): String {
    val marksRxString ="$MARKB\\s+${MARKWORDTO}\\s+(.+?)\\s+$MARKE"
    var res = ""
    val matchResult = marksRxString.toRegex().find(rawText)
    if (matchResult != null) {
      if (matchResult.groups.size > 1) {
        res = matchResult.groups[1]?.value.toString()
      }
    }
    return res
  }
  
  fun cbCopySelected(content: String, control: Job?, callback: (String) -> Unit) {
    if (textTranslated.isEmpty()) {
      val contentUpd = content
        .replace("\\n", EOL)
        .replace("\\s+".toRegex(), " ")
      val found = findTextByMarks(contentUpd)
      if (found.isNotEmpty()) {
        textTranslated = found
        control?.cancel()
        callback(found)
      }
    }
  }
  
  suspend fun controlJob(timeOut: Long, jobWebview: Job?, callback: (String) -> Unit) {
    delay(timeOut)
    jobWebview?.cancel()
    callback("")
  }
  
  //================================================================================================
  lateinit var webView: MutableList<WebView?>
  fun selectContent() {
    webView[0]?.evaluateJavascript("javascript:window.getSelection().selectAllChildren(document.documentElement);",null)
  }
  fun getSelected(callback: (String) -> Unit) {
    webView[0]?.evaluateJavascript("(function(){return window.getSelection().toString()})()") {
      callback(it)
    }
  }
  
  @SuppressLint("SetJavaScriptEnabled")
  @Composable
  fun WebView4Translate(
    control: Job?,
    callback: (String) -> Unit,
    text: String = "",
    lang_of: String = "",
    lang_to: String = ""
  ) {
    webView = remember { mutableListOf(null) }
    
    val text2translate = text.ifEmpty { textOrigin }
    val sLangOf = lang_of.ifEmpty { langOf }
    val sLangTo = lang_to.ifEmpty { langTo }
    val markOfNow: String = MARKMAP[sLangOf] ?: ""
    if (markOfNow.isNotEmpty() && markOfNow.compareTo(MARKWORDOF) != 0) MARKWORDOF = markOfNow
    val markToNow: String = MARKMAP[sLangTo] ?: ""
    if (markToNow.isNotEmpty() && markToNow.compareTo(MARKWORDTO) != 0) MARKWORDTO = markToNow
    val textMarked = "$MARKB $MARKWORDOF \n$text2translate\n $MARKE"
    val textEncoded = encodeText(textMarked)
    val urlFull: String = url
      .replace("{ORIGIN}", textEncoded)
      .replace("{LANGOF}", sLangOf)
      .replace("{LANGTO}", sLangTo)
    Logd("Wv: [$sLangOf]->[$sLangTo] '$text2translate' {$MARKWORDOF}/{$MARKWORDTO}")
    Box(
      modifier = Modifier.fillMaxSize(1f)
    ) {
      // WebView
      AndroidView(
        factory = { context ->
          WebView(context).apply {
            settings.javaScriptEnabled = true // Enable JavaScript
            webView[0] = this
            loadUrl(urlFull)
            webViewClient = object : WebViewClient() {
              
              override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if (pageFinished) {
                  if (textTranslated.trim().isEmpty()) {
                    selectContent()
                    getSelected { cbCopySelected(it, control, callback)}
                  }
                }
              }
              
              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                pageFinished = false
                textTranslated = ""
                super.onPageStarted(view, url, favicon)
              }
              
              override fun onPageFinished(view: WebView?, url: String?) {
                pageFinished = true
                super.onPageFinished(view, url)
              }
            } // webViewClient
          }
        },
      ) // WebView
    }
  }
}