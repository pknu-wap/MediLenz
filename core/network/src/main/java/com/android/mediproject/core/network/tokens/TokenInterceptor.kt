package com.android.mediproject.core.network.tokens

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenServer: TokenServer, private val tokenType: TokenType
) : Interceptor {

    enum class TokenType {
        ACCESS_TOKEN, REFRESH_TOKEN
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (tokenServer.isTokenEmpty()) return chain.proceed(chain.request())

        val token = if (tokenType == TokenType.ACCESS_TOKEN) tokenServer.getAccessToken()
        else tokenServer.getRefreshToken()
        val response = chain.request().newBuilder().header("Authorization", "Bearer $token").build()
        return chain.proceed(response)
    }

}