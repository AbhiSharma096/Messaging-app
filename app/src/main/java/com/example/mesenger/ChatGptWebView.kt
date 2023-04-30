package com.example.mesenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar

class ChatGptWebView : AppCompatActivity() {

      private lateinit var webview : WebView
       private lateinit var progressbar : ProgressBar
       private lateinit var cookieManager : CookieManager
       private lateinit var backbtn : ImageView


      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_chat_gpt_web_view)

            webview = findViewById(R.id.webView)
            progressbar = findViewById(R.id.progressBar)
            backbtn = findViewById(R.id.backButton)


            webview.settings.javaScriptEnabled = true
            CookieManager.getInstance().setAcceptCookie(true)
            webview.loadUrl("https://chat.openai.com/")
            webview.webViewClient = WebViewClient()
            webview.webChromeClient = object : WebChromeClient() {
                  override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        progressbar.progress = newProgress
                        if (newProgress == 100) {
                              progressbar.visibility = View.GONE
                        } else {
                              progressbar.visibility = View.VISIBLE
                        }
                  }
            }

                backbtn.setOnClickListener {
                        onBackPressed()
                }

      }

       @Deprecated("Deprecated in Java")
       override fun onBackPressed() {
              if (webview.canGoBack()) {
                     webview.goBack()
              } else {
                     super.onBackPressed()
              }
       }
}