package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
    private lateinit var viewDetailsButton: Button
    private lateinit var backToHomeButton: Button
    private lateinit var showChartButton: Button
    private var selectedDate: String = ""
    private lateinit var apiService: ApiService
    private lateinit var loggedInUsername: String // Dynamically populated logged-in username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        nameSpinner = findViewById(R.id.nameSpinner)
        itemSpinner = findViewById(R.id.itemSpinner)
        mealSpinner = findViewById(R.id.mealSpinner)
        expenditureInput = findViewById(R.id.expenditureInput)
        priceInput = findViewById(R.id.priceInput)
        submitButton = findViewById(R.id.submitButton)
        dateButton = findViewById(R.id.dateButton)
        dateTextView = findViewById(R.id.dateTextView)
        viewDetailsButton = findViewById(R.id.viewDetailsButton)
        calculatorButton = findViewById(R.id.calculatorButton)
        backToHomeButton = findViewById(R.id.backToHomeButton)
       // showChartButton = findViewById(R.id.showChartButton)

        mealSpinner.isEnabled = false

        // Initialize Retrofit and ApiService
        apiService = RetrofitInstance.api

        // Retrieve logged-in username
        loggedInUsername = getLoggedInUsername()

        // Fetch member names for the logged-in username
        fetchMemberNames(loggedInUsername)

        // Setup spinner and button listeners
        setupSpinnerListeners()
        setupButtonListeners()

        enableSubmitButton()
    }

    private fun getLoggedInUsername(): String {
        // Retrieve the logged-in username from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("username", "guest") ?: "guest"
    }

    private fun fetchMemberNames(username: String) {
        Log.d("Username Check", "Fetching names for username: $username")
        // Call the API to get member names for the logged-in username
        apiService.getMemberNames(username).enqueue(object : Callback<MemberNamesResponse> {
            override fun onResponse(call: Call<MemberNamesResponse>, response: Response<MemberNamesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val memberNamesResponse = response.body()
                    if (memberNamesResponse?.success == true && memberNamesResponse.names.isNotEmpty()) {
                        Log.d("API Response", "Fetched names: ${memberNamesResponse.names}")
                        populateNameSpinner(memberNamesResponse.names)
                    } else {
                        Log.e("API Error", "No names found for $username")
                        Toast.makeText(this@MainActivity, "No member names found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error response: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Failed to fetch member names: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MemberNamesResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API Failure", "Error fetching names: ${t.message}")
            }
        })
    }

    private fun populateNameSpinner(memberNames: List<String>) {
        // Add a default item for the spinner (e.g., "Select Name")
        val spinnerItems = mutableListOf("Select Name").apply { addAll(memberNames) }

        // Create an ArrayAdapter and set it to the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nameSpinner.adapter = adapter
    }

    private fun setupSpinnerListeners() {
        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Meal") {
                    mealSpinner.isEnabled = true
                    priceInput.isEnabled = false
                    priceInput.text.clear()
                } else {
                    mealSpinner.isEnabled = false
                    mealSpinner.setSelection(0)
                    priceInput.isEnabled = true
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
    }

    private fun setupButtonListeners() {
        backToHomeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        submitButton.setOnClickListener {
            showPreviewDialog()
        }

        dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        calculatorButton.setOnClickListener {
            val calculatorPopup = CalculatorPopup(this)
            calculatorPopup.showCalculatorPopup()
        }

//        showChartButton.setOnClickListener {
//            val chart = Chart(this)
//            chart.showChartDialog()
//        }
        viewDetailsButton.setOnClickListener {
            // Show the floating details page (PopupWindow)
            val detailsPopup = DetailsPopup(this)
            detailsPopup.showFloatingDetailsPage()
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
        submitButton.isEnabled = ((priceInput.isEnabled && priceInput.text.isNotEmpty() &&
                nameSpinner.selectedItemPosition != 0 && selectedDate.isNotEmpty()) ||
                (mealSpinner.selectedItemPosition != 0 && nameSpinner.selectedItemPosition != 0 &&
                        selectedDate.isNotEmpty()))
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

    private fun submitForm(name: String, item: String, meal: String, expenditure: String, price: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_LONG).show()
            return
        }

        if (name.isEmpty() || item.isEmpty() || selectedDate.isEmpty() || (price.isEmpty() && meal.isEmpty())) {
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Form Submission", "Submitting with token: $token")
        apiService.submitForm(
            token = token,
            name = name,
            item = item,
            meal = meal,
            expenditure = expenditure,
            price = price,
            date = selectedDate
        ).enqueue(object : Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Form submitted successfully!", Toast.LENGTH_LONG).show()
                    resetForm()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(this@MainActivity, "Submission failed: $errorMessage", Toast.LENGTH_LONG).show()
                    Log.e("API Error", "Error: ${response.message()}\n$errorMessage")
                }
            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("Network Error", "Failure: ${t.message}")
            }
        })
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
