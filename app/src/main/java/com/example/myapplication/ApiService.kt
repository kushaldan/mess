package com.example.myapplication


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Body


interface ApiService {
    @POST("login1.php") // Only the relative path
    fun loginUser(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>
}

