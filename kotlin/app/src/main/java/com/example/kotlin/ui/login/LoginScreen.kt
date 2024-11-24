package com.example.kotlin.ui.login

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin.ui.notes.NotesActivity
import com.example.kotlin.data.api.AuthApiService
import com.example.kotlin.data.api.RetrofitClient
import com.example.kotlin.data.api.AuthManager

@Composable
fun LoginScreen(authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState = authViewModel.loginUiState.value  // Access the state from the ViewModel

    val context = LocalContext.current
    val authManager = AuthManager(context)  // Access AuthManager for saving the token

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable the input if loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable the input if loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable the button if loading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.message != null) {
                Text(
                    text = uiState.message,
                    color = if (uiState.isSuccess) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }

            // Redirect to NotesScreen after successful login
            if (uiState.isSuccess) {
                LaunchedEffect(Unit) {
                    val token = ""
                    if (token != null) {
                        // Save the token using AuthManager
                        authManager.saveAuthToken(token)
                        // Redirect to NotesActivity
                        val intent = Intent(context, NotesActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}
