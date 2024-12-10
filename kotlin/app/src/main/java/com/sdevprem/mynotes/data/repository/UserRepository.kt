package com.sdevprem.mynotes.data.repository

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.sdevprem.mynotes.data.api.UserAPI
import com.sdevprem.mynotes.data.model.user.User
import com.sdevprem.mynotes.data.model.user.UserResponse
import com.sdevprem.mynotes.data.utils.TokenManager
import com.sdevprem.mynotes.ui.MainActivity
import com.sdevprem.mynotes.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import javax.inject.Inject

private const val errorMsg = "Something went wrong"

class UserRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val tokenManager: TokenManager
) {

    val authToken = tokenManager.authToken

    fun registerUser(user: User) = doAuth({ userAPI.signup(user) }, debug = true)

    fun loginUser(user: User) = doAuth({ userAPI.signin(user) }, debug = true)

    private inline fun doAuth(
        crossinline authenticate: suspend () -> Response<UserResponse>,
        debug: Boolean = false // Add a debug flag to enable/disable pop-ups
    ) = flow<NetworkResult<UserResponse>> {
        try {
            val response = authenticate()
            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!
                tokenManager.updateAuthToken(userResponse.token)
                if (debug) debugMessage("Authentication successful: Token = ${userResponse.token}")
                emit(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorText = response.errorBody()!!.charStream().readText()
                if (debug) debugMessage("Error during authentication: $errorText")
                emit(NetworkResult.Error(errorText))
            } else {
                if (debug) debugMessage("Unknown error during authentication")
                emit(NetworkResult.Error(errorMsg))
            }
        } catch (e: Exception) {
            if (debug) debugMessage("Exception: ${e.message}")
            emit(NetworkResult.Error(e.message ?: errorMsg))
        }
    }.flowOn(Dispatchers.IO)
        .onStart {
            if (debug) debugMessage("Starting authentication flow")
            emit(NetworkResult.Loading)
        }

    /**
     * Displays debug messages as Toast pop-ups.
     */
    private fun debugMessage(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(MainActivity.instance, message, Toast.LENGTH_SHORT).show()
        }
    }
}
