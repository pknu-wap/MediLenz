package com.android.mediproject.core.model.user

import io.github.pknujsp.core.annotation.KBindFunc

@KBindFunc
sealed interface AccountState {
    data class SignedIn(val myId: Long, val myNickName: String, val email: String) : AccountState
    object SignedOut : AccountState
    object Unknown : AccountState
}
