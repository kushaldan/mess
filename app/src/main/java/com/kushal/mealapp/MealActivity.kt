package com.kushal.mealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MealActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize MealViewModel
        val mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        // Set Compose content for MealActivity
        setContent {
            val navController = rememberNavController()

            // Navigation for Meal-related pages
            NavHost(
                navController = navController,
                startDestination = "meal_page"
            ) {
                composable("meal_page") {
                    MealScreen(viewModel = mealViewModel)  // Pass MealViewModel to MealScreen
                }
            }
        }
    }
}
