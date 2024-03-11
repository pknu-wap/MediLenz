package com.android.mediproject.feature.intro

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.model.navargs.TOHOME
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUseCase: SignRepository, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
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
        email: String, password: String, checkPassword: String, nickName: String,
    ) {
        if (!checkEmailPasswordRegex(email, password)) {
            signUpFaledWithRegexError()
            return
        }

        if (!password.contentEquals(checkPassword)) {
            isNotEqualPasswordCheck()
            return
        }

        signUp(email, password, nickName)
    }

    private fun checkEmailPasswordRegex(email: String, password: String): Boolean {
        return checkEmailRegex(email) && checkPasswordRegex(password)
    }

    private fun checkEmailRegex(email: String): Boolean {
        return isEmailValid(email)
    }

    private fun checkPasswordRegex(password: String): Boolean {
        return isPasswordValid(password)
    }

    private fun signUp(
        email: String,
        password: String,
        nickName: String,
    ) {
        val pair = initEmailPassword(email, password)
        val (emailCharArray, passwordCharArray) = pair.first to pair.second

        setSignUpState(SignUpState.SigningUp)
        viewModelScope.launch {
            /* signUseCase.signUp(SignUpParameter(emailCharArray, passwordCharArray, nickName)).collect { result ->
                 result.fold(
                     onSuccess = { setSignUpState(SignUpState.SignUpSuccess) },
                     onFailure = { setSignUpState(SignUpState.SignUpFailed(it.message ?: "가입 실패")) },
                 )
             }*//*    withContext(ioDispatcher) {
                    signUpAWS.signUp(
                        SignUpAWS.SignUpRequest(
                            email, password.encodeToByteArray(),
                            CognitoUserAttributes().apply {
                                addAttribute("custom:user_name", nickName)
                            },
                        ),
                    )
                }.onSuccess {
                    setSignUpState(SignUpState.SignUpSuccess)
                }.onFailure {
                    setSignUpState(SignUpState.SignUpFailed(it.message ?: "가입 실패"))
                }*/
        }
        //fillEmailPassword(emailCharArray, passwordCharArray)
    }

    private fun isNotEqualPasswordCheck() {
        setSignUpState(SignUpState.PasswordError)
    }

    private fun initEmailPassword(email: String, password: String): Pair<CharArray, CharArray> {
        return Pair(initEmail(email), initPassword(password))
    }

    private fun initEmail(email: String): CharArray {
        val emailCharArray = CharArray(email.length)
        email.trim().forEachIndexed { index, c ->
            emailCharArray[index] = c
        }
        return emailCharArray
    }

    private fun initPassword(password: String): CharArray {
        val passwordCharArray = CharArray(password.length)
        password.trim().forEachIndexed { index, c ->
            passwordCharArray[index] = c
        }
        return passwordCharArray
    }

    private fun signUpFaledWithRegexError() {
        setSignUpState(SignUpState.RegexError)
    }

    private fun fillEmailPassword(email: CharArray, password: CharArray) {
        email.fill('\u0000')
        password.fill('\u0000')
    }
}
