package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        userIdEditText = findViewById(R.id.userIdEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val userId = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation
            if (userId.isNotEmpty() && password.isNotEmpty()) {
                if (isValidLogin(userId, password)) {
                    // Login successful, navigate to HomeActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optionally finish the LoginActivity so the user cannot go back
                } else {
                    // Show an error message
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show a message if any field is empty
                Toast.makeText(this, "Please enter both user ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This is a placeholder function to validate login credentials
    private fun isValidLogin(userId: String, password: String): Boolean {
        // You can replace this with actual authentication logic (e.g., API call, database check)
        return userId == "user" && password == "password" // Simple hardcoded validation
    }
    private fun onLoginSuccess() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
