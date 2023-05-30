package com.android.mediproject.core.domain

import android.util.Log
import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTokenUseCase @Inject constructor(private val signRepository: SignRepository) {

    suspend operator fun invoke(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        signRepository.getCurrentTokens().collectLatest {
            trySend(it)
        }
    }
}