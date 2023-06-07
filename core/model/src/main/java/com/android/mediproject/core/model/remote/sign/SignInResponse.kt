package com.android.mediproject.core.model.remote.sign


import com.android.mediproject.core.model.awscommon.BaseAwsSignResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("userId") val _userId: Long?,
    @SerialName("nickname") val _nickName: String?,
    @SerialName("email") val _email: String?,
) : BaseAwsSignResponse()