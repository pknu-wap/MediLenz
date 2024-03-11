package com.android.mediproject.feature.intro.signup

import androidx.annotation.StringRes
import com.android.mediproject.feature.intro.R

sealed interface SignUpUiState {
    @get:StringRes val text: Int?

    data object SigningUp : SignUpUiState {
        override val text: Int? = null
    }

    data object RegexError : SignUpUiState {
        override val text: Int = R.string.signInRegexError
    }

    data object PasswordError : SignUpUiState {
        override val text: Int = R.string.signUpPasswordError
    }

    data object UserExists : SignUpUiState {
        override val text: Int = R.string.signUpUserExists
    }

    data object Success : SignUpUiState {
        override val text: Int = R.string.signUpSuccess
    }

    data class Failed(val message: String) : SignUpUiState {
        override val text: Int? = null
    }
}
