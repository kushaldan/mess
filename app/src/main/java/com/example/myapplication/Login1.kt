package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val apiService = RetrofitInstance.api // Assuming RetrofitInstance.api is correctly initialized

        // Create a login request
        val call = apiService.loginUser(username, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status == "success") {
                        // Show success message and move to next screen
                        Toast.makeText(this@Login1, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Start next activity
                        val intent = Intent(this@Login1, SummaryPopup::class.java)
                        startActivity(intent)
                        finish() // Close the login activity to prevent going back
                    } else {
                        Toast.makeText(this@Login1, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle failure, like server errors
                    Toast.makeText(this@Login1, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network failure or other issues
                Toast.makeText(this@Login1, "Login Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
