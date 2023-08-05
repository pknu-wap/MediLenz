package com.android.mediproject.core.model.remote.token

/**
 * 토큰 상태를 나타내는 클래스
 *
 * @see TokenState.AccessExpiration 액세스 만료
 * @see TokenState.RefreshExpiration 리프레시 만료
 * @see TokenState.Valid 유효
 * @see TokenState.Empty 토큰이 없음
 * @see TokenState.Error 에러
 */
sealed class TokenState<out T> {
    data class AccessExpiration<out T>(val data: T) : TokenState<T>()
    data class RefreshExpiration<out T>(val data: T) : TokenState<T>()
    data class Valid<out T>(val data: T) : TokenState<T>()
    object Empty : TokenState<Nothing>()

    data class Error(val exception: Throwable) : TokenState<Nothing>()
}