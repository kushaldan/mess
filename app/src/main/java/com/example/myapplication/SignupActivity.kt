package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
                Toast.makeText(this, "Username must be lowercase letters only", Toast.LENGTH_SHORT).show()
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


            // If validations pass
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
            // Proceed with signup logic (e.g., save data or navigate to another screen)
        }
    }
}
