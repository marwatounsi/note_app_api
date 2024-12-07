package com.example.kotlin.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin.data.api.NotesRepository
import com.example.kotlin.data.api.RetrofitClient

@Composable
fun NotesScreen() {
    // Create the NotesRepository
    val notesRepository = NotesRepository(RetrofitClient.notesApiService)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        
    }
}