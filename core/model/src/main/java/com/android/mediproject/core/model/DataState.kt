package com.android.mediproject.core.model

/**
 * 데이터 상태를 나타내는 클래스
 *
 * @see DataState.Empty
 * @see DataState.Success
 * @see DataState.Error
 */
sealed class DataState<out T> {
    object Empty : DataState<Nothing>()
    data class Success<out R>(val data: R) : DataState<R>()
    data class Error(val message: String) : DataState<Nothing>()
}