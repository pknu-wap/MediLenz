package com.android.mediproject.core.common.viewmodel

/**
 * UI 상태를 나타내는 클래스
 *
 * @see UiState.isInitial
 * @see UiState.isLoading
 * @see UiState.Success
 * @see UiState.Error
 *
 */
sealed class UiState<out T> {
    object isInitial : UiState<Nothing>()
    object isLoading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}