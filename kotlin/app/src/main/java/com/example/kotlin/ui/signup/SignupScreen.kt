package com.example.kotlin.ui.signup

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
import com.example.kotlin.MainActivity

@Composable
fun SignupScreen(signupViewModel: SignupViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState = signupViewModel.signupUiState.value  // Access the state from the ViewModel

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name input field
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable if loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email input field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable if loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password input field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable if loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up button
            Button(
                onClick = { signupViewModel.signup(name, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Disable if loading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Sign Up")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display message (success or error)
            if (uiState.message != null) {
                Text(
                    text = uiState.message,
                    color = if (uiState.isSuccess) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }

            // Redirect to MainActivity on success
            if (uiState.isSuccess) {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}
