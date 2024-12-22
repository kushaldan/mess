package com.example.myapplication

data class LoginResponse(
    val token: String?,   // Token for successful authentication
    val message: String?, // Additional message, like "Login successful" or error details
    val success: Boolean // Indicates if the login was successful (true/false)
)
