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

        submitButton.setOnClickListener {
            val messName = messNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (messName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Handle signup logic here (e.g., send data to a server or save locally)
                Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
