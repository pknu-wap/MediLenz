package com.android.mediproject.core.model.remote.token

/**
 * 토큰 상태를 나타내는 클래스
 *
 * @see TokenState.Expiration 만료
 * @see TokenState.Valid 유효
 * @see TokenState.Error 오류
 */
sealed class TokenState<out T> {
    data class Expiration(val throwable: Throwable) : TokenState<Nothing>()
    data class Valid<out T>(val data: T) : TokenState<T>()
    data class Error(val throwable: Throwable) : TokenState<Nothing>()
}