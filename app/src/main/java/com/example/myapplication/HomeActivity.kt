package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeButton: Button = findViewById(R.id.homeButton)
        //homeButton.setBackgroundColor(Color.parseColor("#00FF00")) // Green color
        val addMealButton: Button = findViewById(R.id.addMealButton)
        val addExpenditureButton: Button = findViewById(R.id.addExpenditureButton)
        val addDepositButton: Button = findViewById(R.id.addDepositButton)
        val viewDetailsButton: Button = findViewById(R.id.viewDetailsButton)
        val chartContainer: FrameLayout = findViewById(R.id.chartContainer)
        val expSummaryButton: Button = findViewById(R.id.expSummmaryButton)
        val placeholderButton:Button = findViewById(R.id.placeholderButton)
        val signUpButton :Button = findViewById(R.id.signUpButton)
        val rootView = findViewById<View>(android.R.id.content)
       // val drawable = homeButton.background
       // if (drawable is GradientDrawable) {
            // Change the background color to green while keeping the rounded corners
            //drawable.setColor(Color.parseColor("#00FF00"))  // Green color
        //}
        // Set a click listener on the button
        signUpButton.setOnClickListener {
            // Create an Intent to navigate to SignupActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent) // Start the SignupActivity
        }


// Change the background color using backgroundTint (this works if the drawable has no solid color)
        homeButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EB940F")) // Green color
        rootView.setBackgroundColor(Color.parseColor("#AAA1CD"));
        // Initialize WebView
        val chartWebView: WebView = WebView(this)

        // Set WebView settings
        chartWebView.settings.javaScriptEnabled = true
        chartWebView.settings.domStorageEnabled = true
        chartWebView.settings.setSupportZoom(true)
        chartWebView.settings.builtInZoomControls = true
        chartWebView.settings.displayZoomControls = false

        // Set WebViewClient to handle page navigation within the WebView
        chartWebView.webViewClient = WebViewClient()

        // Load the chart URL
        chartWebView.loadUrl("https://legalcount.in/meal/chart.php")

        // Set layout parameters
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT // Or set a specific height if needed
        )
        chartWebView.layoutParams = params

        // Add the WebView to the FrameLayout
        chartContainer.addView(chartWebView)

        // Set click listeners for buttons
        homeButton.setOnClickListener {
            if (isLoggedIn()) {
                navigateTo(MainActivity::class.java)
            } else {
                navigateTo(LoginActivity::class.java)
            }
        }

        addMealButton.setOnClickListener {
            navigateTo(AddMealActivity::class.java)
        }

        addExpenditureButton.setOnClickListener {
            navigateTo(AddExpenditureActivity::class.java)
        }

        addDepositButton.setOnClickListener {
            navigateTo(AddDepositActivity::class.java)
        }

        viewDetailsButton.setOnClickListener {
            // Show the floating details page (PopupWindow)
            val detailsPopup = DetailsPopup(this)
            detailsPopup.showFloatingDetailsPage()
        }
        expSummaryButton.setOnClickListener {
            // Show the floating details page (PopupWindow)
            val summaryPopup = SummaryPopup(this)
            summaryPopup.showFloatingDetailsPage()
        }
    }

    // Helper function to avoid repetition of intent creation and start
    private fun <T> navigateTo(destination: Class<T>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }

    // Check if the user is logged in by using SharedPreferences
    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}
