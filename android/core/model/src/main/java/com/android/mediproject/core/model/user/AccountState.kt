package com.android.mediproject.core.model.user

sealed class AccountState {
    data class SignedIn(val myId: Long, val myNickName: String, val email: String) : AccountState()
    object SignedOut : AccountState()
    object Unknown : AccountState()
}