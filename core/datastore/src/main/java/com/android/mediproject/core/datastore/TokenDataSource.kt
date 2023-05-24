package com.android.mediproject.core.datastore

import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.remote.token.ConnectionTokenDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenDataStore: DataStore<ConnectionToken>, private val aesCoder: AesCoder
) {

    val tokens: Flow<Result<ConnectionTokenDto>> = flow {
        tokenDataStore.data.catch { exception ->
            emit(Result.failure(exception))
        }.map { savedToken ->
            savedToken.takeIf {
                it.accessToken.isNotEmpty() && it.refreshToken.isNotEmpty() && it.userId.isNotEmpty() && it.expirationDatetime.isNotEmpty()
            }?.let {
                Result.success(
                    ConnectionTokenDto(
                        accessToken = aesCoder.decode(it.accessToken),
                        refreshToken = aesCoder.decode(it.refreshToken),
                        userEmail = aesCoder.decode(it.userId),
                        expirationDateTime = LocalDateTime.parse(it.expirationDatetime)
                    )
                )
            } ?: Result.failure(Exception("Token is not saved"))
        }
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
            ).setExpirationDatetime(connectionTokenDto.expirationDateTime.toString()).setUserId(
                aesCoder.encode(
                    connectionTokenDto.userEmail
                )
            ).build()
        }
    }

    /**
     * 저장된 토큰을 모두 제거한다.
     */
    suspend fun clearSavedTokens() {
        tokenDataStore.updateData { currentToken ->
            currentToken.toBuilder().setAccessToken("").setRefreshToken("").setExpirationDatetime("").setUserId("").build()
        }
    }
}