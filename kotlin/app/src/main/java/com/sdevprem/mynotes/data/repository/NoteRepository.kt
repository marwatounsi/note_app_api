package com.sdevprem.mynotes.data.repository

import android.util.Log
import com.sdevprem.mynotes.data.api.NotesAPI
import com.sdevprem.mynotes.data.model.notes.Note
import com.sdevprem.mynotes.utils.Constants.TAG
import com.sdevprem.mynotes.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import javax.inject.Inject

const val ERROR_MESSAGE = "Something went wrong"

class NoteRepository @Inject constructor(
    private val notesAPI: NotesAPI
) {

    fun getNotes() = sendRequest { notesAPI.getNotes() }

    fun createNote(note: Note) = sendRequest { notesAPI.createNote(note) }

    fun updateNote(noteId: String, note: Note) =
        sendRequest { notesAPI.updateNote(noteId, note ) }

    fun deleteNote(noteId: String) =
        sendRequest { notesAPI.deleteNote(noteId) }

    private inline fun <T> sendRequest(crossinline request: suspend () -> Response<T>) = flow {
        try {
            handleResponse(request())
        } catch (e: Exception) {
            val errorMsg = e.message ?: ERROR_MESSAGE
            Log.e(TAG, errorMsg)
            emit(NetworkResult.Error(errorMsg))
        }
    }.flowOn(Dispatchers.IO)
        .onStart { emit(NetworkResult.Loading) }

    private suspend fun <T> FlowCollector<NetworkResult<T>>.handleResponse(response: Response<T>) {
        if (response.isSuccessful) {
            emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorMsg = response.errorBody()!!.charStream().readText()
            Log.e(TAG, errorMsg)
            emit(NetworkResult.Error(errorMsg))
        } else emit(NetworkResult.Error(ERROR_MESSAGE))
    }
}