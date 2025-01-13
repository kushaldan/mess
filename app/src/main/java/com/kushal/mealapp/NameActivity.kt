package com.kushal.mealapp

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NameActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        webView = findViewById(R.id.webView)

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true

        // Configure WebView to handle authentication and other interactions
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Handle the URL loading, we want it to stay within the WebView
                return false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                Toast.makeText(this@NameActivity, "Error: ${error?.description}", Toast.LENGTH_SHORT).show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // You can check the URL after the page finishes loading
                // This is where you might trigger specific actions after the page load
            }
        }

        // Load the URL that will handle the name insertion and authentication
        webView.loadUrl("https://www.legalcount.in/meal/insert_data.php")  // Update the URL as needed
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()  // Go back in WebView history if possible
        } else {
            super.onBackPressed()  // If no history, exit the activity
        }
    }
}
