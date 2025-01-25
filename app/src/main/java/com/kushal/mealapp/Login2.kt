package com.kushal.mealapp

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

class Login2 : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)

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
        Log.d("Login1", "API call initiated with username: $username")

        // Make the API call using loginUserWithJson
        apiService.loginUserWithJson(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Hide progress bar
                progressBar.visibility = ProgressBar.INVISIBLE

                // Log API response details for debugging
                Log.d("Login1", "API call response received")
                Log.d("Login1", "Response code: ${response.code()}")
                Log.d("Login1", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("Login1", "LoginResponse: success=${loginResponse?.success}, message=${loginResponse?.message}, token=${loginResponse?.token}")
                    if (loginResponse != null && loginResponse.success) {
                        // Successful login
                        Toast.makeText(this@Login2, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Get the username and token
                        val username = username.toString().trim()
                        val token = loginResponse.token
                        Log.d("Login1", "Received token: $token")

                        // Save the username and token
                        saveLoginData(username, token.toString())

                        // Navigate to NameActivity
                        val intent = Intent(this@Login2, NameActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: To prevent going back to the Login2 activity on back press
                    } else {
                        // Show error message from the server
                        val errorMessage = loginResponse?.message ?: "Invalid username or password"
                        Toast.makeText(this@Login2, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle server-side errors
                    Toast.makeText(this@Login2, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Hide progress bar on failure
                progressBar.visibility = ProgressBar.INVISIBLE

                // Handle network failure or other unexpected errors
                Toast.makeText(this@Login2, "Login Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveLoginData(username: String, token: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs2", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)  // Save the username
        editor.putString("auth_token", token)   // Save the token
        editor.apply()  // Commit changes asynchronously
        Log.d("Login2", "Username and Token saved to SharedPreferences")
    }

}
//token is generated and login is working fine