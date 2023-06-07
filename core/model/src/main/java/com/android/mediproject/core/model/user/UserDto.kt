package com.android.mediproject.core.model.user

data class UserDto(
    val nickName: String
) {
    var id: Long = 0
    var email: String = ""
}