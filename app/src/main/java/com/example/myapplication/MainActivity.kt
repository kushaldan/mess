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

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Initially disable mealSpinner
        mealSpinner.isEnabled = false

        // Handle selection in itemSpinner
        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //mealSpinner.isEnabled = selectedItem == "Meal"

                if (selectedItem == "Meal") {
                    mealSpinner.isEnabled = true
                    priceInput.isEnabled = false  // Disable price input when mealSpinner is enabled
                    priceInput.text.clear()       // Optionally clear price input
                } else {
                    mealSpinner.isEnabled = false
                    mealSpinner.setSelection(0)
                    priceInput.isEnabled = true   // Enable price input when mealSpinner is disabled
                }
                // After changing the state, check if the submit button should be enabled or disabled
                enableSubmitButton()
            // Call this method after the state change
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                mealSpinner.isEnabled = false
                mealSpinner.setSelection(0)
                priceInput.isEnabled = true
                enableSubmitButton()
                // Ensure button status is updated
            }
        }

        // Handle selection in mealSpinner
//        mealSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                itemSpinner.isEnabled = !mealSpinner.isEnabled
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing
//            }
//        }

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
        submitButton.isEnabled = ((
                priceInput.isEnabled &&
                        priceInput.text.isNotEmpty() &&
                        nameSpinner.selectedItemPosition != 0 &&
                        selectedDate.isNotEmpty()
                ) || (
                mealSpinner.selectedItemPosition != 0 &&
                        nameSpinner.selectedItemPosition != 0 &&
                        selectedDate.isNotEmpty()
                ))
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
                return hashMapOf(
                    "name" to name,
                    "item" to item,
                    "meal" to meal,
                    "expenditure" to expenditure,
                    "price" to price,
                    "date" to selectedDate
                )
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
