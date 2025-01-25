package com.kushal.mealapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kushal.mealapp.database.MealScreen
import com.kushal.mealapp.database.MealViewModel

class HomeActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)  // This line still keeps XML views for non-Compose content.

        // Initialize MealViewModel
        val mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

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

        // Update button and rootView colors
        homeButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EB940F"))
        rootView.setBackgroundColor(Color.parseColor("#AAA1CD"))
        placeholderButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EB940F"))
        rootView.setBackgroundColor(Color.parseColor("#AAA1CD"))
        viewDetailsButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00ff00"))
        rootView.setBackgroundColor(Color.parseColor("#AAA1CD"))

        // Button click listeners for actions
        expSummaryButton.setOnClickListener {
            // Navigate to MealScreen using Jetpack Compose
            setContent {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "meal_page"
                ) {
                    composable("meal_page") {
                        MealScreen(viewModel = mealViewModel) // Pass the MealViewModel to MealScreen
                    }
                }
            }
        }

        // WebView initialization for chart
        val chartWebView = WebView(this)
        chartWebView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        // Enable cookies
        CookieManager.getInstance().setAcceptCookie(true)

        // Set a custom WebViewClient to handle login and redirection
        chartWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false // Allow the WebView to handle URL loading
            }
        }

        // Load the chart URL
        chartWebView.loadUrl("https://legalcount.in/meal/chart2.php")

        // Set layout parameters for the WebView
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        chartWebView.layoutParams = params

        // Add the WebView to the FrameLayout
        chartContainer.addView(chartWebView)

        // Button click listeners for other actions
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, Login1::class.java))
        }

        homeButton.setOnClickListener {
            if (isLoggedIn()) {
                navigateTo(MainActivity::class.java)
            } else {
                navigateTo(Login1::class.java)
            }
        }

        addMealButton.setOnClickListener { navigateTo(AddMealActivity::class.java) }
        addExpenditureButton.setOnClickListener { navigateTo(AddExpenditureActivity::class.java) }
        addDepositButton.setOnClickListener { navigateTo(AddDepositActivity::class.java) }
        viewDetailsButton.setOnClickListener { DetailsPopup(this).showFloatingDetailsPage() }
        placeholderButton.setOnClickListener { navigateTo(NameActivity::class.java) }
    }

    // Helper function to navigate between activities
    private fun <T> navigateTo(destination: Class<T>) {
        startActivity(Intent(this, destination))
    }

    // Check if the user is logged in
    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}
