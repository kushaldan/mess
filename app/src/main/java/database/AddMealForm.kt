package com.kushal.mealapp.database

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddMealForm(viewModel: MealViewModel) {
    // Input state variables
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

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
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary
            ),
            isError = amount.toDoubleOrNull() == null // Highlight invalid input
        )
        if (amount.isNotEmpty() && amount.toDoubleOrNull() == null) {
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
                    val meal = Meal(name = name, date = date, amount = amount.toDouble())
                    viewModel.addMeal(meal)

                    // Reset the fields after submission
                    name = ""
                    date = ""
                    amount = ""
                }
            },
            enabled = name.isNotEmpty() && date.isNotEmpty() && amount.toDoubleOrNull() != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Meal")
        }
    }

}
