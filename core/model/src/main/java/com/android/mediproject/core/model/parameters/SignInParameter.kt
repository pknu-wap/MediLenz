package com.android.mediproject.core.model.parameters

data class SignInParameter(
    val email: CharArray, val password: CharArray, val isSavedEmail: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignInParameter

        if (!email.contentEquals(other.email)) return false
        if (!password.contentEquals(other.password)) return false
        if (isSavedEmail != other.isSavedEmail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.contentHashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + isSavedEmail.hashCode()
        return result
    }

}