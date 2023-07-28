package com.android.mediproject.core.data.token

import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getCurrentTokens(): Flow<TokenState<CurrentTokens>>
}
