package com.android.mediproject.core.common.util

private const val EMAIL_REGX: String = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
private const val PW_MIN_LENGTH = 4
private const val PW_MAX_LENGTH = 16

fun isEmailValid(email: CharSequence) = email.matches(
    Regex(EMAIL_REGX),
)

fun isPasswordValid(password: CharSequence) = (password.length in PW_MIN_LENGTH..PW_MAX_LENGTH)
