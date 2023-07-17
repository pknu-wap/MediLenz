package com.android.mediproject.core.data.remote.token

import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getCurrentTokens(): Flow<TokenState<CurrentTokens>>
}
