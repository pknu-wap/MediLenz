package com.android.mediproject.core.model.awscommon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
open class BaseAwsQueryResponse {
    @SerialName("message") val message: String = ""
}