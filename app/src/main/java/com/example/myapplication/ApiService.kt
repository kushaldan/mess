package com.example.myapplication

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // JSON-based request for login
    @Headers("Content-Type: application/json")
    @POST("login1.php") // Relative path for the login endpoint
    fun loginUserWithJson(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    // Form-urlencoded request for login
    @FormUrlEncoded
    @POST("login1.php") // Same relative path for form-based login
    fun loginUserWithForm(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Form submission with token authentication
    @FormUrlEncoded
    @POST("mealupdateapp.php") // Relative path for the meal update endpoint
    fun submitForm(
        @Field("name") name: String,
        @Field("item") item: String,
        @Field("meal") meal: String,
        @Field("expenditure") expenditure: String,
        @Field("price") price: String,
        @Field("date") date: String,
        @Field("token") token: String
    ): Call<JSONObject>
}
