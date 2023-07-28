package com.android.mediproject.core.model

/**
 * 데이터 상태를 나타내는 클래스
 *
 * @see DataState.Empty
 * @see DataState.Success
 * @see DataState.Error
 */
sealed interface DataState<out T> {
    object Empty : DataState<Nothing>
    data class Success<out R>(val data: R) : DataState<R>
    data class Error(val message: String) : DataState<Nothing>
}


fun <T> DataState<T>.isEmpty(block: () -> Unit): DataState<T> {
    if (this is DataState.Empty) block()
    return this
}

fun <T> DataState<T>.isSuccess(block: (T) -> Unit): DataState<T> {
    if (this is DataState.Success) block(data)
    return this
}

fun <T> DataState<T>.isError(block: (String) -> Unit): DataState<T> {
    if (this is DataState.Error) block(message)
    return this
}
