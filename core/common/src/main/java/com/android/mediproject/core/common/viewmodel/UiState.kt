package com.android.mediproject.core.common.viewmodel

import io.github.pknujsp.core.annotation.KBindFunc

/**
 * UI 상태를 나타내는 클래스
 *
 * @see UiState.Initial
 * @see UiState.Loading
 * @see UiState.Success
 * @see UiState.Error
 *
 */
@KBindFunc
sealed interface UiState<out T> {
    object Initial : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
