package com.android.mediproject.core.common.viewmodel

sealed class UiState<out T> {
    object isInitial : UiState<Nothing>()
    object isLoading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}