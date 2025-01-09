package com.example.myapplication

import android.content.Context

object SharedPrefManager {

    private const val PREF_NAME = "AppPrefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_TOKEN = "auth_token"

    /**
     * Retrieves the username and token of the logged-in user as a Pair.
     * @param context The context to access SharedPreferences.
     * @return A Pair containing the username and token. Either value may be null if not set.
     */
    fun getUserCredentials(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val token = sharedPreferences.getString(KEY_TOKEN, null)

        // Log retrieval for debugging (remove in production)
        if (username != null && token != null) {
            println("SharedPrefManager: Retrieved username: $username, token: $token")
        } else {
            println("SharedPrefManager: User credentials not found")
        }

        return Pair(username, token)
    }

    /**
     * Retrieves the username of the logged-in user.
     * @param context The context to access SharedPreferences.
     * @return The username as a String, or null if not set.
     */
    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USERNAME, null).also {
            println("SharedPrefManager: Retrieved username: $it") // Debugging log
        }
    }

    /**
     * Retrieves the authentication token of the logged-in user.
     * @param context The context to access SharedPreferences.
     * @return The token as a String, or null if not set.
     */
    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_TOKEN, null).also {
            println("SharedPrefManager: Retrieved token: $it") // Debugging log
        }
    }

    /**
     * Clears all stored user data. Typically used during logout.
     * @param context The context to access SharedPreferences.
     */
    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        println("SharedPrefManager: Cleared all user data") // Debugging log
    }
}
