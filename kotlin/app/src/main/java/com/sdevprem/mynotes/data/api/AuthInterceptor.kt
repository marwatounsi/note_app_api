package com.sdevprem.mynotes.data.api;

import com.sdevprem.mynotes.data.utils.TokenManager;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.first;
import kotlinx.coroutines.runBlocking;
import okhttp3.Interceptor;
import okhttp3.Response;
import javax.inject.Inject;

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking(Dispatchers.IO) {
        val requestBuilder = chain
            .request()
            .newBuilder()

        // Retrieve the token from the TokenManager
        val token = tokenManager.authToken.first()

        // Add the token as a Cookie header
        if (token != null) {
            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Cookie", "auth_token=$token")
            }
        }

        // Proceed with the request
        return@runBlocking chain.proceed(requestBuilder.build())
    }
}