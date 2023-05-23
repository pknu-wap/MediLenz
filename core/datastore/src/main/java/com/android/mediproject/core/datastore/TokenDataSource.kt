package com.android.mediproject.core.datastore

import androidx.datastore.core.DataStore
import com.android.mediproject.core.model.remote.token.ConnectionTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenDataStore: DataStore<ConnectionToken>,
) {

    val tokens = tokenDataStore.data.catch { exception ->
        TokenState.Error(exception)
    }.map {
        TokenState.Available(
            ConnectionTokenDto(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken,
                myId = it.userId,
            )
        )
    }

    /**
     * 토큰을 저장한다.
     *
     * @param accessToken
     * @param refreshToken
     * @param userId
     */
    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
        userId: String,
    ) {
        tokenDataStore.updateData { currentToken ->
            currentToken.toBuilder().setAccessToken(accessToken).setRefreshToken(refreshToken).setUserId(userId).build()
        }
    }
}