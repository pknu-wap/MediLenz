package com.android.mediproject.core.model.remote.token

/**
 * 토큰 상태를 나타내는 클래스
 *
 * @see TokenState.Expiration
 * @see TokenState.Available
 * @see TokenState.Error
 */
sealed class TokenState<out T> {
    data class Expiration(val throwable: Throwable) : TokenState<Throwable>()
    data class Available<out R>(val data: R) : TokenState<R>()
    data class Error(val throwable: Throwable) : TokenState<Throwable>()
}