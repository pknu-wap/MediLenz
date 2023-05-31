package com.android.mediproject.feature.intro

import MutableEventFlow
import android.text.Editable
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.local.navargs.TOHOME
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.intro.SignInState.FailedSignIn
import com.android.mediproject.feature.intro.SignInState.Initial
import com.android.mediproject.feature.intro.SignInState.RegexError
import com.android.mediproject.feature.intro.SignInState.Signing
import com.android.mediproject.feature.intro.SignInState.SuccessSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUseCase: SignUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val aesCoder: AesCoder,
) : BaseViewModel() {

    val savedEmail = signUseCase.savedEmail.flatMapLatest {
        if (it.isEmpty()) emptyFlow()
        else flowOf(it.toCharArray())
    }.flowOn(ioDispatcher).stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = CharArray(0))

    private val _signInEvent = MutableStateFlow<SignInState>(Initial)
    val signInEvent = _signInEvent.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    private val _moveFlag = MutableStateFlow(TOHOME)
    val moveFlag get() = _moveFlag.asStateFlow()

    fun event(event: SignEvent) = viewModelScope.launch { _eventFlow.emit(event) }
    fun login() = event(SignEvent.SignIn)
    fun signUp() = event(SignEvent.SignUp)
    fun setMoveFlag(flag : Int){ _moveFlag.value = flag }

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
    fun signIn(emailEditable: Editable, passwordEditable: Editable, checkedSaveEmail: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            // 이메일 또는 비밀번호 형식 오류 검사
            if (!isEmailValid(emailEditable)) {
                _signInEvent.value = RegexError
                return@launch
            } else if (isPasswordValid(passwordEditable)) {
                _signInEvent.value = RegexError
                return@launch
            }

            val email = CharArray(emailEditable.length)
            emailEditable.trim().forEachIndexed { index, c ->
                email[index] = c
            }
            val password = CharArray(passwordEditable.length)
            passwordEditable.trim().forEachIndexed { index, c ->
                password[index] = c
            }

            _signInEvent.value = Signing
            // 로그인
            signUseCase.signIn(SignInParameter(email, password, checkedSaveEmail)).collect { result ->
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
        object SignIn : SignEvent()
        object SignUp : SignEvent()
    }


}

/**
 * 로그인 상태
 *
 * @property Initial 로그아웃 상태
 * @property Signing 로그인 중 상태
 * @property RegexError 이메일 또는 비밀번호 형식 오류
 * @property SuccessSignIn 로그인 성공
 * @property FailedSignIn 로그인 실패
 */
sealed class SignInState {
    object Initial : SignInState()
    object Signing : SignInState()

    object RegexError : SignInState()
    object SuccessSignIn : SignInState()
    data class FailedSignIn(val message: String) : SignInState()
}