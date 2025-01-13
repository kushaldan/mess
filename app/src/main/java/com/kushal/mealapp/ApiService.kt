package com.kushal.mealapp

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

    @FormUrlEncoded
        @POST("insert_data.php") // Replace with your actual API endpoint
        fun insertName(
        @Field("name") name: String,
        @Header("Authorization") token: String
        ): Call<ApiResponse>

    companion object

}
