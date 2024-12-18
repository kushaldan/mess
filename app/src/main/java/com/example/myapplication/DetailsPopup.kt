package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.PopupWindow

class DetailsPopup(private val context: Context) {

    fun showFloatingDetailsPage() {
        // Inflate the popup layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_details, null)

        // Initialize WebView and Button
        val detailsWebView: WebView = popupView.findViewById(R.id.detailsWebView)
        val closeButton: Button = popupView.findViewById(R.id.closeButton)

        // Setup WebView
        setupWebView(detailsWebView)

        // Load URL into WebView
        val url = "https://legalcount.in/meal/summary.php"
        detailsWebView.loadUrl(url)

        // Create PopupWindow and show it
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true  // Make the popup focusable to allow back button dismissal
        )

        // Show the PopupWindow centered on the screen
        popupWindow.showAtLocation(
            popupView.rootView,
            android.view.Gravity.CENTER, 0, 0
        )

        // Dim the background
        dimBackground(popupWindow)

        // Set close button action to dismiss the PopupWindow
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun setupWebView(webView: WebView) {
        // Enable JavaScript and configure WebView settings
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true

        // Enable pinch-to-zoom and hide zoom controls for cleaner UI
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        // Set WebViewClient to handle redirects within the WebView itself
        webView.webViewClient = WebViewClient()
    }

    private fun dimBackground(popupWindow: PopupWindow) {
        // Dim the background of the screen when the PopupWindow is shown
        val container = popupWindow.contentView.parent as View
        val layoutParams = container.layoutParams as WindowManager.LayoutParams
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.5f  // Adjust the dim amount (0.0f to 1.0f)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.updateViewLayout(container, layoutParams)
    }
}
