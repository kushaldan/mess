package com.example.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*


class AddMealActivity : AppCompatActivity() {

    private lateinit var nameSpinnerAddMeal: Spinner
    private lateinit var mealSpinnerAddMeal: Spinner
    private lateinit var submitButtonAddMeal: Button
    private lateinit var dateButtonAddMeal: Button
    private lateinit var dateTextViewAddMeal: TextView
    private lateinit var requestQueueAddMeal: RequestQueue
    private lateinit var resetButton:Button
    private var selectedDateAddMeal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        nameSpinnerAddMeal = findViewById(R.id.nameSpinner)
        mealSpinnerAddMeal = findViewById(R.id.mealSpinner)
        submitButtonAddMeal = findViewById(R.id.submitButton)
        dateButtonAddMeal = findViewById(R.id.dateButton)
        dateTextViewAddMeal = findViewById(R.id.dateTextView)
        resetButton = findViewById(R.id.resetButton)
        requestQueueAddMeal = Volley.newRequestQueue(this)

        // Set up date picker button listener
        dateButtonAddMeal.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up button click listeners
        submitButtonAddMeal.setOnClickListener {
            submitForm()
        }
        // Set up reset button listener
        resetButton.setOnClickListener {
            resetForm()
        }

        enableSubmitButton()
    }
    private fun resetForm() {
        // Reset the spinners to default position (index 0)
        nameSpinnerAddMeal.setSelection(0)
        mealSpinnerAddMeal.setSelection(0)

        // Reset the date TextView to the default message
        dateTextViewAddMeal.text = "No date selected"

        // Clear the selected date
        selectedDateAddMeal = ""

        // Disable the submit button until the form is filled out again
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

        // Show the preview dialog before submitting
        showPreviewDialog(name, meal)
    }

    private fun submitForm(name: String, meal: String) {
        val url = "https://legalcount.in/submit_data.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_LONG).show()
                }
                resetForm()
            },
            Response.ErrorListener {
                Toast.makeText(this, "Failed to submit data", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["meal"] = meal
                params["date"] = selectedDateAddMeal
                return params
            }
        }

        requestQueueAddMeal.add(stringRequest)
    }

    private fun showPreviewDialog(name: String, meal: String) {
        // Create the preview message
        val previewMessage = """
        Name: $name
        
        Meal: $meal
        
        Date: $selectedDateAddMeal
    """.trimIndent()

        // Build the AlertDialog for preview
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Preview Your Submission")
        alertDialogBuilder.setMessage(previewMessage)

        // Add buttons
        alertDialogBuilder.setPositiveButton("Submit") { _, _ ->
            // Proceed to submit the form data if user confirms
            submitForm(name, meal)
        }

        alertDialogBuilder.setNegativeButton("Edit") { _, _ ->
            // User wants to go back and edit the form, do nothing, just close the dialog
        }

        // Show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

//    private fun resetForm() {
//        nameSpinnerAddMeal.setSelection(0)
//        mealSpinnerAddMeal.setSelection(0)
//        dateTextViewAddMeal.text = "No date selected"
//        selectedDateAddMeal = ""
//        enableSubmitButton()
//    }

}


//class AddMealActivity : AppCompatActivity() {
//
//    private lateinit var nameSpinner: Spinner
//    private lateinit var mealSpinner: Spinner
//    private lateinit var submitButton: Button
//    private lateinit var dateButton: Button
//    private lateinit var dateTextView: TextView
//    private lateinit var requestQueue: RequestQueue
//    private var selectedDate: String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_meal)
//
//        nameSpinner = findViewById(R.id.nameSpinner)
//        mealSpinner = findViewById(R.id.mealSpinner)
//        submitButton = findViewById(R.id.submitButton)
//        dateButton = findViewById(R.id.dateButton)
//        dateTextView = findViewById(R.id.dateTextView)
//        requestQueue = Volley.newRequestQueue(this)
//
//        // Set up date picker button listener
//        dateButton.setOnClickListener {
//            showDatePickerDialog()
//        }
//
//        // Set up button click listeners
//        submitButton.setOnClickListener {
//            submitForm()
//        }
//
//        // Assuming nameSpinner and mealSpinner are populated elsewhere, either from a static list or dynamic data
//        // Enable or disable the submit button based on the form fields.
//        enableSubmitButton()
//    }
//
//    private fun showDatePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
//            selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
//            dateTextView.text = selectedDate
//            enableSubmitButton()
//        }, year, month, day)
//
//        datePickerDialog.show()
//    }
//
//    private fun enableSubmitButton() {
//        submitButton.isEnabled =
//            mealSpinner.selectedItemPosition != 0 &&
//                    nameSpinner.selectedItemPosition != 0 &&
//                    selectedDate.isNotEmpty()
//    }
//
//    private fun submitForm() {
//        val name = nameSpinner.selectedItem.toString()
//        val meal = mealSpinner.selectedItem.toString()
//
//        // Show the preview dialog before submitting
//        showPreviewDialog(name, meal)
//    }
//
//    private fun submitForm(name: String, meal: String) {
//        val url = "https://legalcount.in/submit_data.php"
//
//        val stringRequest = object : StringRequest(Request.Method.POST, url,
//            Response.Listener { response ->
//                try {
//                    val jsonResponse = JSONObject(response)
//                    val message = jsonResponse.getString("message")
//                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//                } catch (e: Exception) {
//                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_LONG).show()
//                }
//                resetForm()
//            },
//            Response.ErrorListener {
//                Toast.makeText(this, "Failed to submit data", Toast.LENGTH_LONG).show()
//            }) {
//            override fun getParams(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params["name"] = name
//                params["meal"] = meal
//                params["date"] = selectedDate
//                return params
//            }
//        }
//
//        requestQueue.add(stringRequest)
//    }
//
//    private fun showPreviewDialog(name: String, meal: String) {
//        // Create the preview message
//        val previewMessage = """
//        Name: $name
//
//        Meal: $meal
//
//        Date: $selectedDate
//    """.trimIndent()
//
//        // Build the AlertDialog for preview
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle("Preview Your Submission")
//        alertDialogBuilder.setMessage(previewMessage)
//
//        // Add buttons
//        alertDialogBuilder.setPositiveButton("Submit") { _, _ ->
//            // Proceed to submit the form data if user confirms
//            submitForm(name, meal)
//        }
//
//        alertDialogBuilder.setNegativeButton("Edit") { _, _ ->
//            // User wants to go back and edit the form, do nothing, just close the dialog
//        }
//
//        // Show the dialog
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }
//
//    private fun resetForm() {
//        nameSpinner.setSelection(0)
//        mealSpinner.setSelection(0)
//        dateTextView.text = "No date selected"
//        selectedDate = ""
//        enableSubmitButton()
//    }
//}
