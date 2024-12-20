package com.sdevprem.mynotes.data.api

import com.sdevprem.mynotes.data.model.notes.Note
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes(): Response<List<Note>>

    @POST("/notes")
    suspend fun createNote(@Body note: Note): Response<Note>

    @PATCH("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String, @Body note: Note): Response<Note>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<String>
}