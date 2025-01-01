package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ApiClient
import com.example.myapplication.ApiService
import com.example.myapplication.LoginRequest
import com.example.myapplication.LoginResponse
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

        loginButton.setOnClickListener {
            val userId = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation
            if (userId.isNotEmpty() && password.isNotEmpty()) {
                validateLogin(userId, password)
            } else {
                // Show a message if any field is empty
                Toast.makeText(this, "Please enter both user ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(userId: String, password: String) {
        val loginRequest = LoginRequest(userId, password)

        // Use the JSON-based method in the ApiService
        apiService.loginUserWithJson(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        // Login successful, navigate to HomeActivity
                        onLoginSuccess()
                    } else {
                        // Show an error message
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

    private fun onLoginSuccess() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
