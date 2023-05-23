package com.android.mediproject.core.datastore

import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.remote.token.ConnectionTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenDataStore: DataStore<ConnectionToken>, private val aesCoder: AesCoder
) {

    val tokens = tokenDataStore.data.catch { exception ->
        TokenState.Error(exception)
    }.map {
        TokenState.Available(
            ConnectionTokenDto(
                _accessToken = aesCoder.decode(it.accessToken),
                _refreshToken = aesCoder.decode(it.refreshToken),
                _userId = aesCoder.decode(it.userId)
            )
        )
    }

    /**
     * 토큰을 저장한다.
     */
    suspend fun updateTokens(
        connectionTokenDto: ConnectionTokenDto
    ) {
        tokenDataStore.updateData { currentToken ->
            currentToken.toBuilder().setAccessToken(aesCoder.encode(connectionTokenDto.accessToken)).setRefreshToken(
                aesCoder.encode(connectionTokenDto.refreshToken)
            ).setUserId(aesCoder.encode(connectionTokenDto.userId)).build()
        }
    }
}