package com.example.kotlin.ui.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.api.RetrofitClient
import com.example.kotlin.data.models.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

data class SignupUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null
)

class SignupViewModel : ViewModel() {
    var signupUiState = mutableStateOf(SignupUiState())
        private set

    fun signup(name: String, email: String, password: String) {
        signupUiState.value = signupUiState.value.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.authApiService
                    .signup(SignupRequest(name, email, password))
                    .awaitResponse()

                if (response.isSuccessful) {
                    val body = response.body()
                    signupUiState.value = signupUiState.value.copy(
                        isLoading = false,
                        isSuccess = body?.success == true,
                        message = body?.message ?: "Signup successful"
                    )
                } else {
                    signupUiState.value = signupUiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        message = "Error: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                signupUiState.value = signupUiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    message = e.message
                )
            }
        }
    }
}