package com.android.mediproject.core.datastore

import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.NewTokensFromServer
import com.android.mediproject.core.model.remote.token.TokenState

interface TokenDataSource {

    val currentTokenState: TokenState<CurrentTokens>

    /**
     * 토큰을 저장한다.
     */
    suspend fun saveTokensToLocal(newTokensFromServer: NewTokensFromServer)

    /**
     * 저장된 토큰을 모두 제거한다.
     */
    suspend fun removeTokens()


}
