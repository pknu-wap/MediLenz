package com.android.mediproject.core.datastore

import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface TokenDataSource {


    /**
     * 현재 토큰 상태를 가져온다.
     *
     * @return TokenState
     */
    suspend fun currentTokens(): Flow<TokenState<CurrentTokenDto>>

    /**
     * 토큰을 저장한다.
     */
    suspend fun updateTokens(newTokensFromAws: NewTokensFromAws)

    /**
     * 저장된 토큰을 모두 제거한다.
     */
    suspend fun signOut()
}