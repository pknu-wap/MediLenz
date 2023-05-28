package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.intro.SignUpState.FailedSignUp
import com.android.mediproject.feature.intro.SignUpState.PasswordError
import com.android.mediproject.feature.intro.SignUpState.RegexError
import com.android.mediproject.feature.intro.SignUpState.SigningUp
import com.android.mediproject.feature.intro.SignUpState.SuccessSignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUseCase: SignUseCase, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _signUpEvent = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signInEvent = _signUpEvent.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SignUpEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun signUpComplete() = event(SignUpEvent.SignUpComplete)

    fun signUp(
        emailEditable: CharSequence, passwordEditable: CharSequence, checkPasswordEditable: CharSequence, nickNameEditable: CharSequence
    ) {
        viewModelScope.launch(ioDispatcher) {
            // 이메일 또는 비밀번호 형식 오류 검사
            if (!isEmailValid(emailEditable)) {
                _signUpEvent.value = SignUpState.RegexError
                return@launch
            } else if (isPasswordValid(passwordEditable)) {
                _signUpEvent.value = SignUpState.RegexError
                return@launch
            } else if (passwordEditable != checkPasswordEditable) {
                _signUpEvent.value = SignUpState.PasswordError
                return@launch
            }

            val email = CharArray(emailEditable.length).also {
                emailEditable.trim().forEachIndexed { index, c ->
                    it[index] = c
                }
            }

            val password = CharArray(passwordEditable.length).also {
                passwordEditable.trim().forEachIndexed { index, c ->
                    it[index] = c
                }
            }

            _signUpEvent.value = SigningUp
            signUseCase.signUp(SignUpParameter(email, password, nickNameEditable.toString())).collect { result ->
                result.fold(onSuccess = {
                    _signUpEvent.value = SuccessSignUp
                }, onFailure = {
                    _signUpEvent.value = FailedSignUp(it.message ?: "가입 실패")
                })
            }

            // 이메일과 비밀번호 배열 초기화
            email.fill('\u0000')
            password.fill('\u0000')
        }
    }

    sealed class SignUpEvent {
        object SignUpComplete : SignUpEvent()
    }
}

/**
 * 로그인 상태
 *
 * @property SigningUp 가입 처리 중 상태
 * @property RegexError 이메일 또는 비밀번호 형식 오류
 * @property PasswordError 비밀번호 미 일치
 * @property SuccessSignUp 가입 성공
 * @property FailedSignUp 가입 실패
 */
sealed class SignUpState {
    object SigningUp : SignUpState()
    object Initial : SignUpState()
    object RegexError : SignUpState()
    object PasswordError : SignUpState()
    object SuccessSignUp : SignUpState()
    data class FailedSignUp(val message: String) : SignUpState()
}