package com.example.kotlin.data.api

import com.example.kotlin.data.models.Note
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NotesApiService {
    @GET("notes")
    suspend fun getNotes(@Header("Authorization") authHeader: String): Response<List<Note>>
}