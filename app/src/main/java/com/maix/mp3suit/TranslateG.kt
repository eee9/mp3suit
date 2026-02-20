package com.maix.mp3suit

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
  val EOL = "\n"
  val langOf = "en"
  val langTo = "en"
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
  val MARKWORDOF: String = MARKMAP[langOf] ?: "wall"
  val MARKWORDTO: String = MARKMAP[langTo] ?: "mur"

  val textOrigin = "Some spark of text."
  var textTranslated: String = ""
  var pageFinished = false
  
//  val url = "https://example.com/"
    val url = "https://www.wufoo.com/gallery/templates/search/?s={ORIGIN}"
//    val url = "https://translate.google.com/?sl={LANGOF}&tl={LANGTO}&text={ORIGIN}&op=translate"
  
  fun encodeText(text: String): String {
    return URLEncoder.encode(text, StandardCharsets.UTF_8.name())
  }
  fun decodeText(text: String): String {
    return URLDecoder.decode(text, StandardCharsets.UTF_8.name())
  }
  
  val marksRxString ="$MARKB\\s+${MARKWORDTO}\\s+(.+?)\\s+$MARKE"
  fun findTextByMarks(rawText: String): String {
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
  fun WebView4Translate(control: Job?, callback: (String) -> Unit) {
    webView = remember { mutableListOf(null) }
    
    val textMarked = "$MARKB\n $MARKWORDOF \n$textOrigin\n $MARKE"
    val textEncoded = encodeText(textMarked)
    val urlFull: String = url
      .replace("{ORIGIN}", textEncoded)
      .replace("{LANGOF}", langOf)
      .replace("{LANGTO}", langTo)
    Box(
      modifier = Modifier.fillMaxSize(0.01f)
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