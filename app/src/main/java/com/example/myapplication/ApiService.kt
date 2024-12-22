package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    // JSON-based request (using LoginRequest object)
    @Headers("Content-Type: application/json")
    @POST("login1.php") // The relative path for the login endpoint
    fun loginUserWithJson(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    // Form-urlencoded request (alternative approach with Fields)
    @FormUrlEncoded
    @POST("login1.php") // Same relative path for form-based login
    fun loginUserWithForm(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}
