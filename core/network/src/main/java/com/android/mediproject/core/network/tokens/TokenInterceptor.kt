package com.android.mediproject.core.network.tokens

import com.android.mediproject.core.datastore.EndpointTokenState
import com.android.mediproject.core.datastore.TokenServer
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenServer: TokenServer, private val tokenType: TokenType) : Interceptor {

    enum class TokenType {
        ACCESS_TOKEN, REFRESH_TOKEN
    }

    override fun intercept(chain: Interceptor.Chain): Response = tokenServer.currentTokens.let {
        // 저장된 토큰이 없는 경우
        // 로그인, 회원가입을 새롭게 하는 경우
        if (it is EndpointTokenState.NoToken) return chain.proceed(chain.request())
        else {
            // 토큰을 재발급 받는 경우
            // 리프레시 토큰(재발급)을 보낼지 액세스 토큰(댓글, 좋아요 등)을 보낼지 분기
            val tokenOnTokenServer = (it as EndpointTokenState.SavedToken).token
            val token = if (tokenType == TokenType.ACCESS_TOKEN) tokenOnTokenServer.accessToken
            else tokenOnTokenServer.refreshToken

            val response = chain.request().newBuilder().header("Authorization", "Bearer $token").build()
            return chain.proceed(response)
        }
    }


}