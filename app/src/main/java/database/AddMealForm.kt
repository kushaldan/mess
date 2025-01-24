package com.kushal.mealapp.database

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddMealForm(viewModel: MealViewModel) {
    // Input state variables
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary
            )
        )

        // Date Input
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary
            )
        )

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                showError = amount.isNotEmpty() && amount.toDoubleOrNull() == null
            },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary
            ),
            isError = showError // Highlight invalid input
        )
        if (showError) {
            Text(
                text = "Please enter a valid number for the amount",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }

        // Submit Button
        Button(
            onClick = {
                if (name.isNotEmpty() && date.isNotEmpty() && amount.toDoubleOrNull() != null) {
                    val meal = Meal(
                        id = 0,
                        name = name,
                        date = date,
                        amount = amount.toDouble() // Convert amount to Double
                    )
                    viewModel.addMeal(meal)

                    // Reset the fields after submission
                    name = ""
                    date = ""
                    amount = ""
                    showError = false
                }
            },
            enabled = name.isNotEmpty() && date.isNotEmpty() && amount.toDoubleOrNull() != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Meal")
        }
    }
}
