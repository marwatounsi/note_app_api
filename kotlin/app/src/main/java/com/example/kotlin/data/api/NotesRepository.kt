package com.example.kotlin.data.api

import com.example.kotlin.data.models.Note
import retrofit2.Response


class NotesRepository(private val notesApiService: NotesApiService) {

    suspend fun getNotes(authToken: String): Response<List<Note>> {
        return notesApiService.getNotes(authToken)
    }
}