package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenUseCase @Inject constructor(private val signRepository: SignRepository) {

    suspend operator fun invoke(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        send(signRepository.getCurrentTokens())
    }
}