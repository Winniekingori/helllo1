package com.example.helllo1

import okhttp3.Call
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("register")
    fun registerStudent(@Body requestBody: RequestBody): Call<RegistrationResponse>

    @POST("login")
    fun loginStudent(@Body requestBody: RequestBody): Call<LoginResponse>

    @GET("courses")
    fun getCourses(@Header("Authorization") accessToken: String): Call<Course>
}