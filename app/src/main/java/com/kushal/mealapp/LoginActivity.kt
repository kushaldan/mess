package com.kushal.mealapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        userIdEditText = findViewById(R.id.userIdEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Initialize Retrofit service
        apiService = ApiClient.getApiClient().create(ApiService::class.java)

        // Login button click listener
        loginButton.setOnClickListener {
            val username = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation
            if (username.isNotEmpty() && password.isNotEmpty()) {
                validateLogin(username, password)
            } else {
                // Show a message if any field is empty
                Toast.makeText(this, "Please enter both user ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        // Use the JSON-based method in the ApiService
        apiService.loginUserWithJson(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        val token = loginResponse.token
                        if (token != null) {
                            // Save user ID and token if login is successful
                            saveUserId(username, token)
                            onLoginSuccess()
                        } else {
                            // Handle the case where the token is null
                            Toast.makeText(this@LoginActivity, "Token is null. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Show an error message from the response body
                        Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle API errors (e.g., 400, 500)
                    Toast.makeText(this@LoginActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network or server error
                Toast.makeText(this@LoginActivity, "Failed to connect to the server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserId(username: String, token: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("username", username)  // Save the user ID
        editor.putString("token", token)  // Save the authentication token
        editor.apply()  // Commit changes to SharedPreferences
    }

    private fun onLoginSuccess() {
        // Check if a redirection target was provided
        val redirectTo = intent.getStringExtra("redirectTo")
        if (redirectTo != null) {
            try {
                // Dynamically find and navigate to the target activity
                val targetClass = Class.forName("com.example.myapplication.$redirectTo")
                val intent = Intent(this, targetClass)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                // If the class is not found, fall back to MainActivity
                Toast.makeText(this, "Redirect target not found, navigating to MainActivity.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            // Default behavior: Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()  // Close the login activity to avoid returning to it
    }
}
