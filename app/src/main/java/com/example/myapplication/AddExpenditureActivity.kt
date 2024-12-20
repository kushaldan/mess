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

class AddExpenditureActivity : AppCompatActivity() {

    private lateinit var itemSpinner: Spinner
    private lateinit var expenditureInput: EditText
    private lateinit var submitButton: Button
    private lateinit var dateButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var resetButton: Button
    private lateinit var nameTextView: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var mealTextView: TextView
    private lateinit var priceInput: EditText
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenditure)

        // Initialize views
        itemSpinner = findViewById(R.id.itemSpinner)
        expenditureInput = findViewById(R.id.expenditureInput)
        priceInput = findViewById(R.id.priceInput)
        submitButton = findViewById(R.id.submitButton)
        dateButton = findViewById(R.id.dateButton)
        dateTextView = findViewById(R.id.dateTextView)
        resetButton = findViewById(R.id.resetButton)
        requestQueue = Volley.newRequestQueue(this)

        // Set up listeners
        dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        submitButton.setOnClickListener {
            submitForm()
        }

        resetButton.setOnClickListener {
            resetForm()
        }

        enableSubmitButton()
    }

    private fun resetForm() {
        // Reset the spinner to the default position (index 0)
        itemSpinner.setSelection(0)

        // Clear the expenditure input field
        expenditureInput.text.clear()

        // Reset the date TextView to the default message
        dateTextView.text = "No date selected"

        // Clear the selected date
        selectedDate = ""

        // Disable the submit button until the form is filled out again
        enableSubmitButton()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            dateTextView.text = selectedDate
            enableSubmitButton()
        }, year, month, day)

        datePickerDialog.show()

    }

    private fun enableSubmitButton() {
        submitButton.isEnabled = itemSpinner.selectedItemPosition != 0 &&
              //  expenditureInput.text.isNotEmpty() &&
                selectedDate.isNotEmpty()
    }

    private fun submitForm() {
        val item = itemSpinner.selectedItem.toString()
        val expenditure = expenditureInput.text.toString()
        val name = intent.getStringExtra("name") ?: ""
        val meal = intent.getStringExtra("meal") ?: ""
        val price = intent.getStringExtra("price") ?: ""

        // Show the preview dialog before submitting
        showPreviewDialog(name, item, meal, expenditure, price)
    }

    private fun submitForm(name: String, item: String, meal: String, expenditure: String, price: String) {
        val url = "https://legalcount.in/meal/mealupdate.php"

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
                params["name"]=name
                params["item"] = item
                params["meal"]=meal
                params["expenditure"] = expenditure
                params["date"] = selectedDate
                params["price"]=price
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun showPreviewDialog(name: String, item: String, meal: String, expenditure: String, price: String) {
        // Create the preview message
        val previewMessage = """
            Item: $item

            Expenditure: $expenditure

            Date: $selectedDate
        """.trimIndent()

        // Build the AlertDialog for preview
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Preview Your Submission")
        alertDialogBuilder.setMessage(previewMessage)

        // Add buttons
        alertDialogBuilder.setPositiveButton("Submit") { _, _ ->
            submitForm(name, item, meal, expenditure, price)
        }

        alertDialogBuilder.setNegativeButton("Edit") { _, _ ->
            // User wants to go back and edit the form
        }

        // Show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
