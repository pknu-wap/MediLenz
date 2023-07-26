package com.android.mediproject.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class DataGoKrResponse<ITEM : Any> {
    @SerialName("header") val header: Header? = null
    @SerialName("body") val _body: Body<ITEM>? = null

    val body: Body<ITEM>
        get() = _body!!

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String, // 00
        @SerialName("resultMsg") val resultMsg: String, // NORMAL SERVICE.
    )

    @Serializable
    data class Body<ITEM : Any>(
        @SerialName("items") val items: List<ITEM> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0,
        @SerialName("pageNo") val pageNo: Int = 0,
        @SerialName("totalCount") val totalCount: Int = 0,
    )

}

inline fun <reified T : DataGoKrResponse<*>> T.toResult(): Result<T> = header?.run {
    if (resultCode == "00") Result.success(this@toResult)
    else Result.failure(Throwable(resultMsg))
} ?: Result.failure(Throwable("Response Failed"))
