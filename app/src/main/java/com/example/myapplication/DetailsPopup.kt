package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.PopupWindow


class DetailsPopup(private val context: Context) {

    fun showFloatingDetailsPage() {
        // Inflate the popup layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_details, null)

        // Set up the WebView inside the PopupWindow
        val detailsWebView: WebView = popupView.findViewById(R.id.detailsWebView)
        val closeButton: Button = popupView.findViewById(R.id.closeButton)

        // Set up WebView for displaying details
        detailsWebView.settings.javaScriptEnabled = true
        detailsWebView.settings.loadWithOverviewMode = true
        detailsWebView.settings.useWideViewPort = true
        detailsWebView.webViewClient = WebViewClient()

        // Enable zoom controls and pinch-to-zoom
        detailsWebView.settings.builtInZoomControls = true
        detailsWebView.settings.displayZoomControls = false  // Hide zoom buttons for a cleaner UI

        // Load a URL or content into the WebView
        val url = "https://legalcount.in/meal/summary.php"
        detailsWebView.loadUrl(url)

        // Create and show the PopupWindow in full-screen
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true  // Focusable to allow back button dismissal
        )

        // Ensure the popup covers the entire screen
        popupWindow.isClippingEnabled = false

        // Show the PopupWindow
        popupWindow.showAtLocation(
            popupView.rootView,
            android.view.Gravity.CENTER, 0, 0
        )

        // Dim the background behind the popup
        val container = popupWindow.contentView.parent as View
        val layoutParams = container.layoutParams as WindowManager.LayoutParams
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.5f
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.updateViewLayout(container, layoutParams)

        // Close button action
        closeButton.setOnClickListener {
            popupWindow.dismiss()  // Close the PopupWindow
        }
    }
}

//
//class DetailsPopup(private val context: Context) {
//
//    fun showFloatingDetailsPage() {
//        // Inflate the popup layout
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val popupView = inflater.inflate(R.layout.popup_details, null)
//
//        // Set up the WebView inside the PopupWindow
//        val detailsWebView: WebView = popupView.findViewById(R.id.detailsWebView)
//        val closeButton: Button = popupView.findViewById(R.id.closeButton)
//
//        // Set up WebView for displaying details
//        detailsWebView.settings.javaScriptEnabled = true
//        detailsWebView.settings.loadWithOverviewMode = true
//        detailsWebView.settings.useWideViewPort = true
//        detailsWebView.webViewClient = WebViewClient()
//
//        // Load a URL or content into the WebView
//        val url = "https://legalcount.in/mess_tv.php"
//        detailsWebView.loadUrl(url)
//
//        // Create and show the PopupWindow in full-screen
//        val popupWindow = PopupWindow(
//            popupView,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            true  // Focusable to allow back button dismissal
//        )
//
//        // Ensure the popup covers the entire screen
//        popupWindow.isClippingEnabled = false
//
//        // Show the PopupWindow
//        popupWindow.showAtLocation(
//            popupView.rootView,
//            android.view.Gravity.CENTER, 0, 0
//        )
//
//        // Dim the background behind the popup
//        val container = popupWindow.contentView.parent as View
//        val layoutParams = container.layoutParams as WindowManager.LayoutParams
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
//        layoutParams.dimAmount = 0.5f
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        windowManager.updateViewLayout(container, layoutParams)
//
//        // Close button action
//        closeButton.setOnClickListener {
//            popupWindow.dismiss()  // Close the PopupWindow
//        }
//    }
//}
