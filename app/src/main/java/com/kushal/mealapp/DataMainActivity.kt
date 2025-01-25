package com.kushal.mealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

class DataMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = (application as MyApp).database.mealDao()
        val viewModel = MealViewModel(dao)

        setContent {
            MaterialTheme {
                Column {
                    MealScreen(viewModel)
                    AddMealForm(viewModel)
                }
            }
        }
    }
}
