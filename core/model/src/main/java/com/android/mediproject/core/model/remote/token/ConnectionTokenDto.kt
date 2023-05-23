package com.android.mediproject.core.model.remote.token

data class ConnectionTokenDto(
    private val _accessToken: CharArray,
    private val _refreshToken: CharArray,
    private val _userId: CharArray,
) {

    val accessToken: String

    val refreshToken: String

    val userId: String

    init {
        accessToken = _accessToken.toStr()
        refreshToken = _refreshToken.toStr()
        userId = _userId.toStr()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConnectionTokenDto

        if (!_accessToken.contentEquals(other._accessToken)) return false
        if (!_refreshToken.contentEquals(other._refreshToken)) return false
        if (!_userId.contentEquals(other._userId)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _accessToken.contentHashCode()
        result = 31 * result + _refreshToken.contentHashCode()
        result = 31 * result + _userId.contentHashCode()
        return result
    }

    private fun CharArray.toStr(): String {
        return this.joinToString("")
    }
}