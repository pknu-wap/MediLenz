package com.android.mediproject.core.common.viewmodel

/**
 * UI 상태를 나타내는 클래스
 *
 * @see UiState.Init
 * @see UiState.Loading
 * @see UiState.Success
 * @see UiState.Error
 *
 */
sealed interface UiState<out T> {
    object Init : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<out R>(val data: R) : UiState<R>
    data class Error(val message: String) : UiState<Nothing>
}

fun <T> UiState<T>.isInitalizing(block: () -> Unit): UiState<T> {
    if (this is UiState.Init) block()
    return this
}

fun <T> UiState<T>.isLoading(block: () -> Unit): UiState<T> {
    if (this is UiState.Loading) block()
    return this
}

fun <T> UiState<T>.isSuccess(block: (T) -> Unit): UiState<T> {
    if (this is UiState.Success) block(data)
    return this
}

fun <T> UiState<T>.isError(block: (String) -> Unit): UiState<T> {
    if (this is UiState.Error) block(message)
    return this
}
