package com.kushal.mealapp

// Response for the login request
data class LoginResponse(
    val token: String?,   // Token for successful authentication
    val message: String?, // Additional message, like "Login successful" or error details
    val success: Boolean, // Indicates if the login was successful (true/false)
    val username: String? // The username of the logged-in user
)

// Response containing a list of member names
data class MemberNamesResponse(
    val success: Boolean,
    val names: List<String>, // List of member names
    val username: String?    // The username of the logged-in user
)

// Request data class for submitting name and username
data class NameRequest(
    val username: String, // The username of the logged-in user
    val name: String      // The name to be inserted
)

// Response for inserting a name
data class InsertNameResponse(
    val success: Boolean, // Indicates if the insert operation was successful
    val message: String?  // Message from the server, e.g., "Name inserted successfully"
)

// General API response format
data class ApiResponse(
    val success: Boolean, // Whether the operation was successful
    val message: String   // Message returned from the API
)
