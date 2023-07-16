package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.local.navargs.TOHOME
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUseCase: SignUseCase, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signUpState = _signUpState.asStateFlow()

    private fun setSignUpState(state: SignUpState) {
        _signUpState.value = state
    }

    sealed class SignUpState {
        object SigningUp : SignUpState()
        object Initial : SignUpState()
        object RegexError : SignUpState()
        object PasswordError : SignUpState()
        object SignUpSuccess : SignUpState()
        data class SignUpFailed(val message: String) : SignUpState()
    }

    private val _eventFlow = MutableEventFlow<SignUpEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SignUpEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun signUp() = event(SignUpEvent.SignUp)


    sealed class SignUpEvent {
        object SignUp : SignUpEvent()
    }

    private val _callBackMoveFlag = MutableStateFlow(TOHOME)
    val callBackMoveFlag get() = _callBackMoveFlag.asStateFlow()

    fun setCallBackMoveFlag(flag: Int) {
        _callBackMoveFlag.value = flag
    }

    fun signUpWithCheckRegex(
        emailEditable: CharSequence, passwordEditable: CharSequence, checkPasswordEditable: CharSequence, nickNameEditable: CharSequence,
    ) {
        if (checkEmailPasswordRegex(emailEditable, passwordEditable)) {
            if (passwordEditable.contentEquals(checkPasswordEditable)) {
                signUp(emailEditable, passwordEditable, nickNameEditable)
            } else {
                isNotEqualPasswordCheck()
            }
        } else {
            signUpFaledWithRegexError()
        }
    }

    private fun checkEmailPasswordRegex(emailEditable: CharSequence, passwordEditable: CharSequence): Boolean {
        return checkEmailRegex(emailEditable) && checkPasswordRegex(passwordEditable)
    }

    private fun checkEmailRegex(emailEditable: CharSequence): Boolean {
        return !isEmailValid(emailEditable)
    }

    private fun checkPasswordRegex(passwordEditable: CharSequence): Boolean {
        return !isPasswordValid(passwordEditable)
    }

    private fun signUp(
        emailEditable: CharSequence,
        passwordEditable: CharSequence,
        nickNameEditable: CharSequence,
    ) {
        val pair = initEmailPasswordCharArray(emailEditable, passwordEditable)
        val (email, password) = pair.first to pair.second

        setSignUpState(SignUpState.SigningUp)
        viewModelScope.launch(ioDispatcher) {
            signUseCase.signUp(SignUpParameter(email, password, nickNameEditable.toString())).collect { result ->
                result.fold(
                    onSuccess = { setSignUpState(SignUpState.SignUpSuccess) },
                    onFailure = { setSignUpState(SignUpState.SignUpFailed(it.message ?: "가입 실패")) },
                )
            }
        }
        clearEmailPasswordCharArray(email, password)
    }

    private fun isNotEqualPasswordCheck() {
        setSignUpState(SignUpState.PasswordError)
    }

    private fun initEmailPasswordCharArray(emailEditable: CharSequence, passwordEditable: CharSequence): Pair<CharArray, CharArray> {
        return Pair(initEmailCharArray(emailEditable), initPasswordCharArray(passwordEditable))
    }

    private fun initEmailCharArray(emailEditable: CharSequence): CharArray {
        val email = CharArray(emailEditable.length)
        emailEditable.trim().forEachIndexed { index, c ->
            email[index] = c
        }
        return email
    }

    private fun initPasswordCharArray(passwordEditable: CharSequence): CharArray {
        val password = CharArray(passwordEditable.length)
        passwordEditable.trim().forEachIndexed { index, c ->
            password[index] = c
        }
        return password
    }

    private fun signUpFaledWithRegexError() {
        setSignUpState(SignUpState.RegexError)
    }

    private fun clearEmailPasswordCharArray(email: CharArray, password: CharArray) {
        email.fill('\u0000')
        password.fill('\u0000')
    }
}
