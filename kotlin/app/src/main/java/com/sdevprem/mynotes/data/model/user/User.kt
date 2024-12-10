package com.sdevprem.mynotes.data.model.user

data class User(
    val userName: String,
    val password: String,
    val email: String,
    val _id: String = ""
)
