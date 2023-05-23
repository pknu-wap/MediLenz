package com.android.mediproject.core.model.remote.token

data class ConnectionTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val myId: String,
)