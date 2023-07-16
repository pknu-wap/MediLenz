package com.android.mediproject.core.common.util

private val emailReg: String by lazy {
    "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
}

fun isEmailValid(email: CharSequence) = email.matches(
    Regex(emailReg)
)

fun isPasswordValid(password: CharSequence) = (password.length in 4..16)
