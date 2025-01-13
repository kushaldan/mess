package com.kushal.mealapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Base URL common to both login and meal update APIs
    private const val BASE_URL = "https://legalcount.in/meal/"

    // Lazy initialization of the Retrofit API service
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
