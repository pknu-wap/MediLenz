package com.android.mediproject.core.model.requestparameters

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordParamter(
    val newPassword: CharArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChangePasswordParamter

        if (!newPassword.contentEquals(other.newPassword)) return false
        return true
    }

    override fun hashCode(): Int {
        return newPassword.contentHashCode()
    }
}
