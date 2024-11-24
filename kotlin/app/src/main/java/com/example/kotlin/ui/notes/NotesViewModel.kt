package com.example.kotlin.ui.notes


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.api.AuthManager
import com.example.kotlin.data.api.NotesRepository
import com.example.kotlin.data.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: NotesRepository, private val authManager: AuthManager) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            try {
                // Retrieve the auth token
                val authToken = authManager.getAuthToken()

                if (authToken != null) {
                    val response = notesRepository.getNotes("Bearer $authToken")
                    if (response.isSuccessful) {
                        _notes.value = response.body() ?: emptyList()
                    } else {
                        // Handle error
                        Log.e("NotesViewModel", "Error fetching notes: ${response.message()}")
                    }
                } else {
                    // Handle missing token error (e.g., log out the user)
                    Log.e("NotesViewModel", "No auth token found")
                }
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Exception: ${e.message}")
            }
        }
    }
}