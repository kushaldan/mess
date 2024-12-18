
package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient

class Chart(context: Context) {

    private val chartWebView = WebView(context)

    init {
        // Enable JavaScript for the WebView
        chartWebView.settings.javaScriptEnabled = true

        // Set WebViewClient to ensure the URL loads within the WebView and not in an external browser
        chartWebView.webViewClient = WebViewClient()

        // Load the chart from the PHP page hosted on the local server
        // Use '10.0.2.2' for Android Emulator to access the local server
        chartWebView.loadUrl("https://legalcount.in/meal/chart.php")
    }

    // Function to show the chart in a dialog
    fun showChartDialog() {
        val dialogBuilder = AlertDialog.Builder(chartWebView.context)
        dialogBuilder.setTitle("Chart")
        dialogBuilder.setView(chartWebView)
        dialogBuilder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.show()
    }
}


//class Chart(context: Context) {
//
//    private val chartWebView = WebView(context)
//
//    init {
//        // Enable JavaScript for the WebView
//        chartWebView.settings.javaScriptEnabled = true
//
//        // Load the chart HTML from the assets folder
//        chartWebView.loadUrl("file:///android_asset/chart.html")
//    }
//
//    // Function to show the chart in a dialog
//    fun showChartDialog() {
//        val dialogBuilder = AlertDialog.Builder(chartWebView.context)
//        dialogBuilder.setTitle("Chart")
//        dialogBuilder.setView(chartWebView)
//        dialogBuilder.setPositiveButton("Close") { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        dialogBuilder.show()
//    }
//}
