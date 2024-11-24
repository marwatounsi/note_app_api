package com.example.kotlin.ui.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.api.RetrofitClient
import com.example.kotlin.data.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val authToken: String? = null
)

class AuthViewModel(private val context: Context) : ViewModel() {
    var loginUiState = mutableStateOf(LoginUiState())
        private set

    fun login(email: String, password: String) {
        loginUiState.value = loginUiState.value.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.authApiService
                    .login(LoginRequest(email, password))
                    .awaitResponse()

                if (response.isSuccessful) {
                    val body = response.body()
                    val authToken = body?.token

                    if (!authToken.isNullOrEmpty()) {
                        saveAuthToken(authToken)
                    }

                    loginUiState.value = loginUiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Login successful!",
                        authToken = authToken
                    )
                } else {
                    loginUiState.value = loginUiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        message = "Error: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                loginUiState.value = loginUiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    message = e.message
                )
            }
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
}
