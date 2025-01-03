package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R.id
import org.json.JSONObject
import java.util.*
import android.util.Log




class MainActivity : AppCompatActivity() {

    private lateinit var calculatorButton: Button
    private lateinit var nameSpinner: Spinner
    private lateinit var itemSpinner: Spinner
    private lateinit var mealSpinner: Spinner
    private lateinit var expenditureInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var submitButton: Button
    private lateinit var dateButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var viewDetailsButton: Button
    private lateinit var backToHomeButton: Button
    private lateinit var showChartButton: Button
    private lateinit var logoutButton: Button
    private var selectedDate: String = ""

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        nameSpinner = findViewById(id.nameSpinner)
        itemSpinner = findViewById(id.itemSpinner)
        mealSpinner = findViewById(id.mealSpinner)
        expenditureInput = findViewById(id.expenditureInput)
        priceInput = findViewById(id.priceInput)
        submitButton = findViewById(id.submitButton)
        dateButton = findViewById(id.dateButton)
        dateTextView = findViewById(id.dateTextView)
        viewDetailsButton = findViewById(id.viewDetailsButton)
        calculatorButton = findViewById(id.calculatorButton)
        backToHomeButton = findViewById(id.backToHomeButton)
        showChartButton = findViewById(id.showChartButton)
        logoutButton = findViewById(R.id.logoutButton)

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Initially disable mealSpinner
        mealSpinner.isEnabled = false

        // Handle selection in itemSpinner
        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem == "Meal") {
                    mealSpinner.isEnabled = true
                    priceInput.isEnabled = false  // Disable price input when mealSpinner is enabled
                    priceInput.text.clear()       // Optionally clear price input
                } else {
                    mealSpinner.isEnabled = false
                    mealSpinner.setSelection(0)
                    priceInput.isEnabled = true   // Enable price input when mealSpinner is disabled
                }
                enableSubmitButton()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mealSpinner.isEnabled = false
                mealSpinner.setSelection(0)
                priceInput.isEnabled = true
                enableSubmitButton()
            }
        }

        // Set up button click listeners
        backToHomeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        submitButton.setOnClickListener {
            showPreviewDialog()
        }

        viewDetailsButton.setOnClickListener {
            val detailsPopup = DetailsPopup(this)
            detailsPopup.showFloatingDetailsPage()
        }

        dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        calculatorButton.setOnClickListener {
            val calculatorPopup = CalculatorPopup(this)
            calculatorPopup.showCalculatorPopup()
        }

        showChartButton.setOnClickListener {
            val chart = Chart(this)
            chart.showChartDialog()
        }

        enableSubmitButton()
        logoutButton.setOnClickListener {
            // Clear the session (SharedPreferences)
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)  // Set the logged-in status to false
            editor.apply()

            // Redirect to LoginActivity after logging out
            val intent = Intent(this, Login1::class.java)
            startActivity(intent)
            finish()  // Close the current activity so user can't go back to the main screen
        }
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
        submitButton.isEnabled = ((priceInput.isEnabled &&
                priceInput.text.isNotEmpty() &&
                nameSpinner.selectedItemPosition != 0 &&
                selectedDate.isNotEmpty())
                || (mealSpinner.selectedItemPosition != 0 &&
                nameSpinner.selectedItemPosition != 0 &&
                selectedDate.isNotEmpty()))
    }

    private fun submitForm(name: String, item: String, meal: String, expenditure: String, price: String) {
        // Fetch the saved token from SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        // Log the token for debugging purposes
        Log.d("AuthToken", "Token retrieved: $token")

        if (token.isNullOrEmpty()) {
            Log.e("AuthTokenError", "Token is missing or expired.")
            Toast.makeText(this, "Unauthorized: Missing or expired token", Toast.LENGTH_LONG).show()
            // Redirect to login activity
            val intent = Intent(this, Login1::class.java)
            startActivity(intent)
            finish()
            return
        }

        val url = "https://legalcount.in/meal/mealupdateapp.php"

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
                return hashMapOf(
                    "name" to name,
                    "item" to item,
                    "meal" to meal,
                    "expenditure" to expenditure,
                    "price" to price,
                    "date" to selectedDate
                )
            }

            // Include the token in the headers for authorization
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Authorization"] = "Bearer $token"  // Add token to headers
                return headers
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun showPreviewDialog() {
        val name = nameSpinner.selectedItem.toString()
        val item = itemSpinner.selectedItem.toString()
        val meal = mealSpinner.selectedItem.toString()
        val expenditure = expenditureInput.text.toString()
        val price = priceInput.text.toString()

        val previewMessage = """
            Name: $name
            Item: $item
            Meal: $meal
            Expenditure: $expenditure
            Price: $price
            Date: $selectedDate
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Preview Your Submission")
            .setMessage(previewMessage)
            .setPositiveButton("Submit") { _, _ ->
                submitForm(name, item, meal, expenditure, price)
            }
            .setNegativeButton("Edit", null)
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun resetForm() {
        expenditureInput.text.clear()
        priceInput.text.clear()
        nameSpinner.setSelection(0)
        itemSpinner.setSelection(0)
        mealSpinner.setSelection(0)
        dateTextView.text = "No date selected"
        selectedDate = ""
        enableSubmitButton()
    }
}
