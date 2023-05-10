package com.android.mediproject.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class DataGoKrBaseResponse(
) {
    @SerialName("header") val header: Header? = null

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String, // 00
        @SerialName("resultMsg") val resultMsg: String // NORMAL SERVICE.
    )

    fun isSuccess(): DataGoKrResult = header?.let { header ->
        if (header.resultCode == "00") {
            DataGoKrResult.isSuccess
        } else {
            DataGoKrResult.isFailure(header.resultMsg)
        }
    } ?: DataGoKrResult.isFailure("Server Response Failed")

}

sealed class DataGoKrResult(val failedMessage: String) {
    object isSuccess : DataGoKrResult("")
    class isFailure(failedMessage: String) : DataGoKrResult(failedMessage)
}