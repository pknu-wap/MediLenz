package com.android.mediproject.feature.intro

import MutableEventFlow
import android.text.Editable
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.intro.SignInState.FailedSignIn
import com.android.mediproject.feature.intro.SignInState.RegexError
import com.android.mediproject.feature.intro.SignInState.SignOut
import com.android.mediproject.feature.intro.SignInState.Signing
import com.android.mediproject.feature.intro.SignInState.SuccessSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUseCase: SignUseCase
) : BaseViewModel() {

    private val _signInEvent = MutableStateFlow<SignInState>(SignOut)
    val signInEvent = _signInEvent.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SignEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(SignEvent.SignIn())
    fun signUp() = event(SignEvent.SignUp())

    private val emailReg = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"

    /**
     * 로그인 요청
     *
     * @param emailEditable 이메일
     * @param passwordEditable 비밀번호
     *
     * 1. 이메일 또는 비밀번호 형식 오류 검사
     * 2. 로그인
     * 3. 로그인 결과 이벤트 발생
     */
    fun signIn(emailEditable: Editable, passwordEditable: Editable) {
        viewModelScope.launch {
            // 이메일 또는 비밀번호 형식 오류 검사
            if (!emailEditable.matches(Regex(emailReg))) {
                _signInEvent.value = RegexError
                return@launch
            } else if (passwordEditable.length !in 4..16) {
                _signInEvent.value = RegexError
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

            _signInEvent.value = Signing

            // 로그인
            signUseCase.signIn(SignInParameter(email, password)).collect { result ->
                result.fold(onSuccess = {
                    _signInEvent.value = SuccessSignIn
                }, onFailure = {
                    _signInEvent.value = FailedSignIn(it.message ?: "로그인 실패")
                })
            }

            // 이메일과 비밀번호 배열 초기화
            email.fill('\u0000')
            password.fill('\u0000')
        }
    }

    sealed class SignEvent {
        data class SignIn(val sign: Unit? = null) : SignEvent()
        data class SignUp(val unit: Unit? = null) : SignEvent()
    }


}

/**
 * 로그인 상태
 *
 * @property SignOut 로그아웃 상태
 * @property Signing 로그인 중 상태
 * @property RegexError 이메일 또는 비밀번호 형식 오류
 * @property SuccessSignIn 로그인 성공
 * @property FailedSignIn 로그인 실패
 */
sealed class SignInState {
    object SignOut : SignInState()
    object Signing : SignInState()

    object RegexError : SignInState()
    object SuccessSignIn : SignInState()
    data class FailedSignIn(val message: String) : SignInState()
}