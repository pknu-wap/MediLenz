package com.android.mediproject.feature.intro.login

import androidx.annotation.StringRes
import com.android.mediproject.feature.intro.R

sealed interface LoginUiState {
    @get:StringRes val text: Int?

    data object NotVerified : LoginUiState {
        override val text: Int = R.string.verificationCodeDescription
    }

    data object RegexError : LoginUiState {
        override val text: Int = R.string.signInRegexError
    }

    data object Success : LoginUiState {
        override val text: Int = R.string.signInSuccess
    }

    data class Failed(val message: String) : LoginUiState {
        override val text: Int? = null
    }
}
