package com.android.mediproject.core.model.remote.sign


import com.android.mediproject.core.model.awscommon.BaseAwsSignResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("id") val id: Long,
) : BaseAwsSignResponse()