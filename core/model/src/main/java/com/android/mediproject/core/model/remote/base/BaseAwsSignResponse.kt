package com.android.mediproject.core.model.remote.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseAwsSignResponse() {
    @SerialName("access_token") val accessToken: String? = null
    @SerialName("refresh_token") val refreshToken: String? = null
    @SerialName("message") val message: String = ""

    fun isSuccess(): Boolean {
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }
}