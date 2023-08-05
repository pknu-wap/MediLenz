package com.android.mediproject.core.model.requestparameters

import kotlinx.serialization.Serializable

@Serializable
data class ChangeNicknameParameter(
    val newNickname : String
)
