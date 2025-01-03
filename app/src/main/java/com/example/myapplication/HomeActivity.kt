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
        val addMealButton: Button = findViewById(R.id.addMealButton)
        val addExpenditureButton: Button = findViewById(R.id.addExpenditureButton)
        val addDepositButton: Button = findViewById(R.id.addDepositButton)
        val viewDetailsButton: Button = findViewById(R.id.viewDetailsButton)
        val chartContainer: FrameLayout = findViewById(R.id.chartContainer)
        val expSummaryButton: Button = findViewById(R.id.expSummmaryButton)
        val placeholderButton: Button = findViewById(R.id.placeholderButton)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val loginButton: Button = findViewById(R.id.loginButton)
        val rootView = findViewById<View>(android.R.id.content)

        // Background and UI settings
        homeButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EB940F"))
        rootView.setBackgroundColor(Color.parseColor("#AAA1CD"))

        // WebView setup
        val chartWebView: WebView = WebView(this)
        chartWebView.settings.javaScriptEnabled = true
        chartWebView.settings.domStorageEnabled = true
        chartWebView.settings.setSupportZoom(true)
        chartWebView.settings.builtInZoomControls = true
        chartWebView.settings.displayZoomControls = false
        chartWebView.webViewClient = WebViewClient()
        chartWebView.loadUrl("https://legalcount.in/meal/chart.php")
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        chartWebView.layoutParams = params
        chartContainer.addView(chartWebView)

        // Redirect to LoginActivity when clicking the Home button
        homeButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("redirectTo", "MainActivity") // Pass redirection target
            startActivity(intent)
        }

        // Other button click listeners
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, Login1::class.java)
            startActivity(intent)
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
            val detailsPopup = DetailsPopup(this)
            detailsPopup.showFloatingDetailsPage()
        }

        expSummaryButton.setOnClickListener {
            val summaryPopup = SummaryPopup(this)
            summaryPopup.showFloatingDetailsPage()
        }
    }

    // Helper function to avoid repetition of intent creation and start
    private fun <T> navigateTo(destination: Class<T>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }
}
