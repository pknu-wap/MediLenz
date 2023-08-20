package com.android.mediproject.core.model.user

data class User(
    val nickName: String,
    val profileUrl: String,
    var id: Long = 0,
    var email: String = "",
)
