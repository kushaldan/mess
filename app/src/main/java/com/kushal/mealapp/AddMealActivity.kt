package com.kushal.mealapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddMealActivity : AppCompatActivity() {

    private lateinit var nameSpinnerAddMeal: Spinner
    private lateinit var mealSpinnerAddMeal: Spinner
    private lateinit var submitButtonAddMeal: Button
    private lateinit var dateButtonAddMeal: Button
    private lateinit var dateTextViewAddMeal: TextView
    private lateinit var resetButton: Button
    private lateinit var mealDatabaseHelper: MealDatabaseHelper
    private lateinit var viewMealsButton: Button
    private var selectedDateAddMeal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)
        mealDatabaseHelper = MealDatabaseHelper(this)

        nameSpinnerAddMeal = findViewById(R.id.nameSpinner)
        mealSpinnerAddMeal = findViewById(R.id.mealSpinner)
        submitButtonAddMeal = findViewById(R.id.submitButton)
        dateButtonAddMeal = findViewById(R.id.dateButton)
        dateTextViewAddMeal = findViewById(R.id.dateTextView)
        resetButton = findViewById(R.id.resetButton)
        viewMealsButton = findViewById(R.id.viewMealsButton)
        dateButtonAddMeal.setOnClickListener {
            showDatePickerDialog()
        }

        submitButtonAddMeal.setOnClickListener {
            submitForm()
        }
        viewMealsButton.setOnClickListener {
            val intent = Intent(this, MealRecyclerView::class.java)
            startActivity(intent)
        }
        resetButton.setOnClickListener {
            resetForm()
        }

        enableSubmitButton()
    }

    private fun resetForm() {
        nameSpinnerAddMeal.setSelection(0)
        mealSpinnerAddMeal.setSelection(0)
        dateTextViewAddMeal.text = "No date selected"
        selectedDateAddMeal = ""
        enableSubmitButton()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDateAddMeal = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            dateTextViewAddMeal.text = selectedDateAddMeal
            enableSubmitButton()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun enableSubmitButton() {
        submitButtonAddMeal.isEnabled =
            mealSpinnerAddMeal.selectedItemPosition != 0 &&
                    nameSpinnerAddMeal.selectedItemPosition != 0 &&
                    selectedDateAddMeal.isNotEmpty()
    }

    private fun submitForm() {
        val name = nameSpinnerAddMeal.selectedItem.toString()
        val meal = mealSpinnerAddMeal.selectedItem.toString()

        // Save data locally
        saveDataLocally(name, meal)
        resetForm()
    }

    private fun saveDataLocally(name: String, meal: String) {
        val isInserted = mealDatabaseHelper.insertMeal(name, meal, selectedDateAddMeal)
        if (isInserted) {
            Toast.makeText(this, "Data saved locally", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Failed to save data locally", Toast.LENGTH_SHORT).show()
    }

}
