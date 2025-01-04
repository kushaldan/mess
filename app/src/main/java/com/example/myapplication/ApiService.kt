package com.example.myapplication

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // JSON-based login request
    @Headers("Content-Type: application/json")
    @POST("login1.php") // Endpoint for user login
    fun loginUserWithJson(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    // Form-urlencoded login request
    @FormUrlEncoded
    @POST("login1.php")
    fun loginUserWithForm(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Form submission with token authentication
    @FormUrlEncoded
    @POST("mealupdateapp.php") // Endpoint for meal updates
    fun submitForm(
        @Field("name") name: String,
        @Field("item") item: String,
        @Field("meal") meal: String,
        @Field("expenditure") expenditure: String,
        @Field("price") price: String,
        @Field("date") date: String,
        @Field("token") token: String
    ): Call<JSONObject>

    // Fetch member names for the logged-in user
    @GET("getnames.php")
    fun getMemberNames(
        @Query("username") username: String
    ): Call<MemberNamesResponse>

    // API for inserting a name with token-based authentication
    @POST("insert_name") // Replace with your actual endpoint
    fun insertName(
        @Header("Authorization") token: String,  // Token passed in the Authorization header
        @Body nameRequest: String // Data model for the request body
    ): Call<InsertNameResponse> // Response model for the API response
}
