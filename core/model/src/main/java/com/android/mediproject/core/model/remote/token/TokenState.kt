package com.android.mediproject.core.model.remote.token

/**
 * 토큰 상태를 나타내는 클래스
 *
 * @see TokenState.Tokens.AccessExpiration 액세스 만료
 * @see TokenState.Tokens.RefreshExpiration 리프레시 만료
 * @see TokenState.Tokens.Valid 유효
 * @see TokenState.Empty 토큰이 없음
 * @see TokenState.Error 에러
 */
sealed interface TokenState<out T : Any> {
    object Empty : TokenState<Nothing>
    data class Error(val exception: Throwable) : TokenState<Nothing>

    sealed class Tokens<out T : Any>(val data: T) : TokenState<T> {
        class AccessExpiration<out T : Any>(data: T) : Tokens<T>(data)
        class RefreshExpiration<out T : Any>(data: T) : Tokens<T>(data)
        class Valid<out T : Any>(data: T) : Tokens<T>(data)
    }
}
