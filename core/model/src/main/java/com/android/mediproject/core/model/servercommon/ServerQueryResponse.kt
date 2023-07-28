package com.android.mediproject.core.model.servercommon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
open class ServerQueryResponse {
    @SerialName("message") val message: String = ""
}
