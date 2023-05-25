package com.android.mediproject.core.model.parameters

data class SignInParameter(
    val email: CharArray, val password: CharArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignInParameter

        if (!email.contentEquals(other.email)) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.contentHashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }

}