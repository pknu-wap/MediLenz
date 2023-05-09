package com.android.mediproject.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class DataGoKrBaseResponse(
) {
    @SerialName("header") val header: Header? = null

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String, // 00
        @SerialName("resultMsg") val resultMsg: String // NORMAL SERVICE.
    )

    fun isSuccess() = header?.let { header ->
        header.resultCode == "00"
    } ?: false
}