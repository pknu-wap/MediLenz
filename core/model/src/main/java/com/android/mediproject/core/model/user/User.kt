package com.android.mediproject.core.model.user

data class User(
    val nickName: String,
) {
    var id: Long = 0
    var email: String = ""
}
