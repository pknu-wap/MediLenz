package com.android.mediproject.core.network.tokens

import android.util.Log
import com.android.mediproject.core.model.remote.token.TokenState
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenRequestInterceptor @Inject constructor(
    private val tokenServer: TokenServer, private val tokenType: TokenType,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = when (val tokenState = tokenServer.tokenState) {
        is TokenState.Tokens -> {
            // 리프레시 토큰(재발급)을 보낼지 액세스 토큰(댓글, 좋아요 등)을 보낼지 분기
            val token = when (tokenType) {
                is TokenType.AccessToken -> tokenState.data.accessToken
                is TokenType.RefreshToken -> tokenState.data.refreshToken
            }

            Log.d("wap", "intercept, tokenType : $tokenType token : $tokenState.data")
            val response = chain.request().newBuilder().header("Authorization", "Bearer ${token.joinToString("")}").build()
            chain.proceed(response)
        }

        else -> {
            // 저장된 토큰이 없는 경우
            // 로그인, 회원가입을 새롭게 하는 경우
            Log.d("wap", "intercept, 저장된 토큰 없음")
            chain.proceed(chain.request())
        }
    }

    sealed interface TokenType {
        object AccessToken : TokenType
        object RefreshToken : TokenType
    }
}
