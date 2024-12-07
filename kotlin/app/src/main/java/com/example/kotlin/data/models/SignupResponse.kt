package com.example.kotlin.data.models

data class SignupResponse(
    val success: Boolean,
    val message: String,
    val token: String?
)