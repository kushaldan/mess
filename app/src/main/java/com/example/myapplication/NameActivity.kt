package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NameActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var userNameText: EditText
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this)

        // Find views by ID
        nameEditText = findViewById(R.id.nameEditText)
        submitButton = findViewById(R.id.submitButton)
        userNameText = findViewById(R.id.userNameText)

        // Retrieve username and token from SharedPrefManager
        val (username, token) = SharedPrefManager.getUserCredentials(this)

        // Log the values for debugging
        Log.d("NameActivity", "Username: $username, Token: $token")

        // Ensure the user is authenticated
        if (username.isNullOrEmpty() || token.isNullOrEmpty()) {
            // Redirect to the login page if credentials are missing
            redirectToLogin()
            return
        }

        // Prefill the username field with the logged-in user's username
        userNameText.setText(username)

        // Handle the submit button click to insert name into the database
        submitButton.setOnClickListener {
            val nameToInsert = nameEditText.text.toString()
            val usernameToInsert = userNameText.text.toString()
            if (nameToInsert.isNotEmpty() && usernameToInsert.isNotEmpty()) {
                submitName(usernameToInsert, nameToInsert, token)
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login2::class.java)
        startActivity(intent)
        finish()
    }

    private fun submitName(username: String, name: String, token: String) {
        val url = "https://legalcount.in/meal/insert_name" // Replace with your actual API endpoint

        // Create JSON object for the request body
        val params = JSONObject()
        params.put("username", username)
        params.put("name", name)

        // Create a JSON object request
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, params,
            Response.Listener { response ->
                try {
                    val success = response.getBoolean("success")
                    val message = response.getString("message")
                    if (success) {
                        Toast.makeText(this, "Name submitted successfully: $message", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to submit name: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // Add the request to the Volley RequestQueue
        requestQueue.add(jsonObjectRequest)
    }
}
