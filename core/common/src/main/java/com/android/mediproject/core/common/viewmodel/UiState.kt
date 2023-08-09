package com.android.mediproject.core.common.viewmodel

/**
 * UI 상태를 나타내는 클래스
 *
 * @see UiState.Initial
 * @see UiState.Loading
 * @see UiState.Success
 * @see UiState.Error
 *
 */
sealed interface UiState<out T> {
    object Initial : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<out R>(val data: R) : UiState<R>
    data class Error(val message: String) : UiState<Nothing>
}
