package com.android.mediproject.core.model.sign


data class SignUpParameter(
    val email: String, val password: ByteArray, val nickName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignUpParameter

        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false
        if (nickName != other.nickName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + nickName.hashCode()
        return result
    }

}
