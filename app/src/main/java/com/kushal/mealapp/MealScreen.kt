package com.kushal.mealapp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme

@Composable

fun MealScreen(viewModel: MealViewModel) {
    // Collecting state
    val meals by viewModel.allMeals.collectAsState(initial = emptyList())
    val deposits by viewModel.allDeposits.collectAsState(initial = emptyList())

    Column(Modifier.padding(16.dp)) {
        Text("Meals", style = MaterialTheme.typography.h6)
        LazyColumn {
            // Using items() to display a list of meals
            items(meals) { meal ->
                Text("Name: ${meal.name}, Date: ${meal.date}, Amount: ${meal.amount}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Deposits", style = MaterialTheme.typography.h6)
        LazyColumn {
            // Using items() to display a list of deposits
            items(deposits) { deposit ->
                Text("Name: ${deposit.name}, Date: ${deposit.date}, Amount: ${deposit.amount}")
            }
        }
    }
}
