package com.android.mediproject.core.model.requestparameters

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordParameter(
    val newPassword: CharArray
) {

    var email: CharArray = CharArray(0)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChangePasswordParameter

        if (!newPassword.contentEquals(other.newPassword)) return false
        if (!email.contentEquals(other.email)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = newPassword.contentHashCode()
        result = 31 * result + email.contentHashCode()
        return result
    }

}
