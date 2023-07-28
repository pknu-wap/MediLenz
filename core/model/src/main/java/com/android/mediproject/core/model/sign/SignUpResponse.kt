package com.android.mediproject.core.model.sign


import com.android.mediproject.core.model.servercommon.ServerSignResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("userId") val _userId: Long?,
    @SerialName("nickname") val _nickName: String?,
    @SerialName("email") val _email: String?,
) : ServerSignResponse()
