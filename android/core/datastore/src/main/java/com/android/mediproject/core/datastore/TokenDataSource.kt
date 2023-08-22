package com.android.mediproject.core.datastore

import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import com.android.mediproject.core.model.remote.token.TokenState

interface TokenDataSource {

    val currentTokenState: TokenState<CurrentTokenDto>

    /**
     * 토큰을 저장한다.
     */
    suspend fun saveTokensToLocal(newTokensFromAws: NewTokensFromAws)

    /**
     * 저장된 토큰을 모두 제거한다.
     */
    suspend fun removeTokens()


}