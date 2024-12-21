package com.example.myapplication


data class LoginResponse(
    val status: String, // "success" or "error"
    val message: String // Additional message
)
