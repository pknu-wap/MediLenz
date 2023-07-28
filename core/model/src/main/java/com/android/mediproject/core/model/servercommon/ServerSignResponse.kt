package com.android.mediproject.core.model.servercommon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ServerSignResponse {
    @SerialName("access_token") val accessToken: String? = null
    @SerialName("refresh_token") val refreshToken: String? = null
    @SerialName("message") val message: String = ""

    fun isSuccess(): Boolean = !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()

}
