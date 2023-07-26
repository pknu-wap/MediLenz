package com.android.mediproject.core.domain

import android.util.Log
import com.android.mediproject.core.data.token.TokenRepository
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTokenUseCase @Inject constructor(private val tokenRepository: TokenRepository) {

    operator fun invoke(): Flow<TokenState<CurrentTokens>> = channelFlow {
        tokenRepository.getCurrentTokens().collectLatest {
            Log.d("wap", "GetTokenUseCase$it")
            trySend(it)
        }
    }
}
