package com.android.mediproject.core.network

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.toResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response

/**
 * Retrofit Response Wrapper
 *
 * @param T Response Body Type
 *
 * HTTP응답이 성공이면, 응답 객체(T)를 반환하고, 실패이면 실패 이유를 반환한다.
 */
internal inline fun <reified T : Any> Response<T>.onResponse(): Result<T> {
    return if (isSuccessful) {
        body()?.let { body ->
            Result.success(body)
        } ?: Result.failure(Throwable("Response Body is Null"))
    } else {
        Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))
    }
}

private val json = Json { coerceInputValues = true }

private inline fun <reified T : Any> String.parse(): Result<T> = try {
    Result.success(json.decodeFromString(this))
} catch (e: Exception) {
    Result.failure(e)
}

internal inline fun <reified T : DataGoKrResponse<*>> Response<String>.onStringResponse(): Result<PairResponse<T, String>> {
    return if (isSuccessful) {
        body()?.parse<T>()?.getOrNull()?.toResult()?.fold(
            onSuccess = { Result.success(PairResponse(it, body()!!)) },
            onFailure = { Result.failure(it) },
        ) ?: Result.failure(Throwable("Response Body is Null"))
    } else {
        Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))
    }
}

internal inline fun <reified T : DataGoKrResponse<*>> Response<T>.onDataGokrResponse(): Result<T> =
    if (isSuccessful) body()?.toResult() ?: Result.failure(Throwable("Response Body is Null"))
    else Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))


internal data class PairResponse<T : Any, E : Any>(
    val first: T,
    val second: E,
)
