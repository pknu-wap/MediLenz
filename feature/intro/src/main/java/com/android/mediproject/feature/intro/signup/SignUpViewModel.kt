package com.android.mediproject.feature.intro.signup

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.data.sign.SignUpState
import com.android.mediproject.core.model.navargs.TOHOME
import com.android.mediproject.core.model.sign.SignUpParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignRepository, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val mutableSignUpUiState = MutableEventFlow<SignUpUiState>(replay = 1)
    val signUpUiState = mutableSignUpUiState.asEventFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    private val _callBackMoveFlag = MutableStateFlow(TOHOME)
    val callBackMoveFlag = _callBackMoveFlag.asStateFlow()

    private fun setSignUpState(state: SignUpUiState) {
        viewModelScope.launch { mutableSignUpUiState.emit(state) }
    }

    fun event(event: SignUpEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun signUp() = event(SignUpEvent.SignUp)

    fun setCallBackMoveFlag(flag: Int) {
        _callBackMoveFlag.value = flag
    }

    fun signUpWithCheckRegex(
        email: String, password: String, checkPassword: String, nickName: String,
    ) {
        viewModelScope.launch {
            setSignUpState(SignUpUiState.SigningUp)

            if (!checkEmailPasswordRegex(email, password)) {
                signUpFaledWithRegexError()
                return@launch
            }
            if (!password.contentEquals(checkPassword)) {
                isNotEqualPasswordCheck()
                return@launch
            }

            signUp(email, password, nickName)
        }
    }

    private fun checkEmailPasswordRegex(email: String, password: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password)
    }

    private fun checkEmailRegex(email: String): Boolean {
        return isEmailValid(email)
    }

    private fun checkPasswordRegex(password: String): Boolean {
        return isPasswordValid(password)
    }

    private suspend fun signUp(
        email: String,
        password: String,
        nickName: String,
    ) {
        val pair = initEmailPassword(email, password)
        val signUpState = withContext(ioDispatcher) {
            signUpRepository.signUp(SignUpParameter(email, pair.second, nickName))
        }

        when (signUpState) {
            is SignUpState.Success -> setSignUpState(SignUpUiState.Success)
            is SignUpState.UserExists -> setSignUpState(SignUpUiState.UserExists)
            is SignUpState.Failed -> setSignUpState(SignUpUiState.Failed(signUpState.exception.message ?: ""))
        }
    }

    private fun isNotEqualPasswordCheck() {
        setSignUpState(SignUpUiState.PasswordError)
    }

    private fun initEmailPassword(email: String, password: String): Pair<CharArray, ByteArray> {
        return Pair(initEmail(email), initPassword(password))
    }

    private fun initEmail(email: String): CharArray {
        val emailCharArray = CharArray(email.length)
        email.trim().forEachIndexed { index, c ->
            emailCharArray[index] = c
        }
        return emailCharArray
    }

    private fun initPassword(password: String): ByteArray {
        return password.trim().toByteArray()
    }

    private fun signUpFaledWithRegexError() {
        setSignUpState(SignUpUiState.RegexError)
    }

    private fun fillEmailPassword(email: CharArray, password: CharArray) {
        email.fill('\u0000')
        password.fill('\u0000')
    }


    sealed interface SignUpEvent {
        data object SignUp : SignUpEvent
    }

}
