package com.example.kotlin.data.models

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String
)