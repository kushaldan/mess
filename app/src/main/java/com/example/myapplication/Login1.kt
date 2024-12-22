package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login1 : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar) // Ensure you have a ProgressBar in your layout

        // Set up the login button click listener
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validate input fields
            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        // Show progress bar while making the API call
        progressBar.visibility = ProgressBar.VISIBLE

        // Get Retrofit API service instance
        val apiService = RetrofitInstance.api

        // Create the LoginRequest object
        val loginRequest = LoginRequest(username, password)

        // Make the API call using loginUserWithJson
        apiService.loginUserWithJson(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Hide progress bar
                progressBar.visibility = ProgressBar.INVISIBLE

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        // Successful login
                        Toast.makeText(this@Login1, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to the next screen
                        Log.d("Login1", "Navigating to SummaryPopup")
                        val intent = Intent(this@Login1, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Close the login activity
                    } else {
                        // Show error message from the server
                        val errorMessage = loginResponse?.message ?: "Invalid username or password"
                        Toast.makeText(this@Login1, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle server-side errors
                    Toast.makeText(this@Login1, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Hide progress bar on failure
                progressBar.visibility = ProgressBar.INVISIBLE

                // Handle network failure or other unexpected errors
                Toast.makeText(this@Login1, "Login Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
