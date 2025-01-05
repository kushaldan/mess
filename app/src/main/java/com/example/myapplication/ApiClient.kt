package com.example.myapplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object ApiClient {
    private const val BASE_URL = "https://legalcount.in/meal/"

    // Create the logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Log the full request and response bodies
    }

    // Create OkHttpClient with logging interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // Add the logging interceptor
        .build()

    // Create Gson with lenient mode to tolerate malformed JSON
    private val gson: Gson = GsonBuilder()
        .setLenient()  // Enable lenient mode
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Use the OkHttp client with logging
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson with lenient mode
            .build()
            .create(ApiService::class.java)
    }

    // Optional: If you want to get the Retrofit client instance manually
    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)  // Set the base URL
            .client(client)  // Use the OkHttp client with logging
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson with lenient mode
            .build()  // Build the Retrofit instance
    }
}
