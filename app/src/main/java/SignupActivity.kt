package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val messNameEditText = findViewById<EditText>(R.id.etMessName)
        val usernameEditText = findViewById<EditText>(R.id.etUsername)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val submitButton = findViewById<Button>(R.id.btnSubmit)

        // Listener for the Submit Button
        submitButton.setOnClickListener {
            val messName = messNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if fields are empty
            if (messName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate username: must be lowercase
            if (!username.matches(Regex("^[a-z]+\$"))) {
                Toast.makeText(this, "Username must be lowercase letters only", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Validate password: At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character
            if (!password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"))) {
                Toast.makeText(
                    this,
                    "Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // If validations pass, submit the form
            submitForm(messName, username, password)
        }
    }

    private fun submitForm(messName: String, username: String, password: String) {
        val url = "https://legalcount.in/meal/signup.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Failed to submit data", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "messName" to messName,
                    "username" to username,
                    "password" to password
                )
            }
        }

        // Add the request to the request queue
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
