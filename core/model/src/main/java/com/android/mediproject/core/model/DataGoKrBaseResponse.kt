package com.android.mediproject.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class DataGoKrBaseResponse {
    @SerialName("header") val header: Header? = null

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String, // 00
        @SerialName("resultMsg") val resultMsg: String, // NORMAL SERVICE.
    )

}

inline fun <reified T : DataGoKrBaseResponse> T.toResult(): Result<T> = header?.run {
    if (resultCode == "00") Result.success(this@toResult as T)
    else Result.failure(Throwable(resultMsg))
} ?: Result.failure(Throwable("Response Failed"))
