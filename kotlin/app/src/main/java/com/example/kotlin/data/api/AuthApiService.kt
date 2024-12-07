package com.example.kotlin.data.api

import com.example.kotlin.data.models.LoginRequest
import com.example.kotlin.data.models.LoginResponse
import com.example.kotlin.data.models.SignupRequest
import com.example.kotlin.data.models.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>
}
