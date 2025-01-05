package com.example.myapplication

data class LoginResponse(
    val token: String?,   // Token for successful authentication
    val message: String?, // Additional message, like "Login successful" or error details
    val success: Boolean, // Indicates if the login was successful (true/false)
    val username: String?
)
data class MemberNamesResponse(
    val success: Boolean,
    val names: List<String>,
    val username: String?
)
data class NameRequest(
    val username: String,
    val name: String
)
data class InsertNameResponse(
    val success: Boolean,
    val message: String?
)

data class nameRequest(
    val nameRequest: nameRequest,

    )
data class ApiResponse(
    val success: Boolean,
    val message: String
)
