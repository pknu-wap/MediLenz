package com.android.mediproject.core.network

import retrofit2.Response

/**
 * Retrofit Response Wrapper
 *
 * @param T Response Body Type
 *
 * HTTP응답이 성공이면, 응답 객체(T)를 반환하고, 실패이면 실패 이유를 반환한다.
 */
fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let { body ->
            return Result.success(body)
        } ?: return Result.failure(Throwable("Response Body is Null"))
    } else {
        return Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))
    }
}