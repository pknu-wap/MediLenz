package com.android.mediproject.core.network.retrofitext

import io.github.pknujsp.core.annotation.KBindFunc

@KBindFunc
sealed interface NetworkApiResult<out T> {
    data class Success<out T>(val data: T) : NetworkApiResult<T>

    sealed class Failure(open val exception: Throwable) : NetworkApiResult<Nothing> {
        class ApiError(exception: Throwable) : Failure(exception)
        class NetworkError(exception: Throwable) : Failure(exception)
        class UnknownError(exception: Throwable) : Failure(exception)
    }
}
