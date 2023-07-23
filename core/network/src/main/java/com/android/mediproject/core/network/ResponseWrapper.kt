package com.android.mediproject.core.network

import retrofit2.Response

/**
 * Retrofit Response Wrapper
 *
 * @param T Response Body Type
 *
 * HTTP응답이 성공이면, 응답 객체(T)를 반환하고, 실패이면 실패 이유를 반환한다.
 */
inline fun <reified T : Any> Response<T>.onResponse(): Result<T> {
    return if (isSuccessful) {
        body()?.let { body ->
            Result.success(body)
        } ?: Result.failure(Throwable("Response Body is Null"))
    } else {
        Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))
    }
}
