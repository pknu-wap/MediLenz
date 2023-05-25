package com.android.mediproject.core.network.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestParameter(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("nickname") val nickName: String,
)