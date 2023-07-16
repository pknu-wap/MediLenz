package com.android.mediproject.feature.intro

import MutableEventFlow
import android.text.Editable
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.local.navargs.TOHOME
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.ui.base.BaseViewModel
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
) : BaseViewModel() {

    val savedEmail = signUseCase.savedEmail.flatMapLatest {
        if (it.isEmpty()) emptyFlow()
        else flowOf(it.toCharArray())
    }.flowOn(ioDispatcher).stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = CharArray(0))

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState = _loginState.asStateFlow()

    private fun setLoginState(state: LoginState) {
        _loginState.value = state
    }

    sealed class LoginState {
        object Initial : LoginState()
        object Logining : LoginState()
        object RegexError : LoginState()
        object LoginSuccess : LoginState()
        data class LoginFailed(val message: String) : LoginState()
    }

    private val _callBackMoveFlag = MutableStateFlow(TOHOME)
    val callBackMoveFlag get() = _callBackMoveFlag.asStateFlow()

    fun setMoveFlag(flag: Int) {
        _callBackMoveFlag.value = flag
    }

    private val _eventFlow = MutableEventFlow<LoginEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: LoginEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun loginWithCheckRegex() = event(LoginEvent.Login)

    fun signUp() = event(LoginEvent.SignUp)

    sealed class LoginEvent {
        object Login : LoginEvent()
        object SignUp : LoginEvent()
    }

    fun loginWithCheckRegex(emailEditable: Editable, passwordEditable: Editable, checkedSaveEmail: Boolean) {
        if (!checkEmailPasswordRegex(emailEditable, passwordEditable)) {
            login(emailEditable, passwordEditable, checkedSaveEmail)
        } else {
            loginFaledWithRegexError()
        }
    }

    private fun clearEmailPasswordCharArray(email: CharArray, password: CharArray) {
        email.fill('\u0000')
        password.fill('\u0000')
    }

    private fun checkEmailPasswordRegex(emailEditable: Editable, passwordEditable: Editable): Boolean {
        return checkEmailRegex(emailEditable) && checkPasswordRegex(passwordEditable)
    }

    private fun checkEmailRegex(emailEditable: Editable): Boolean {
        return !isEmailValid(emailEditable)
    }

    private fun checkPasswordRegex(passwordEditable: Editable): Boolean {
        return !isPasswordValid(passwordEditable)
    }

    private fun login(emailEditable: Editable, passwordEditable: Editable, checkedSaveEmail: Boolean) =
        viewModelScope.launch(ioDispatcher) {
            val pair = initEmailPasswordCharArray(emailEditable, passwordEditable)
            val (email, password) = pair.first to pair.second

            setLoginState(LoginState.Logining)

            signUseCase.login(LoginParameter(email, password, checkedSaveEmail)).collect { result ->
                result.fold(
                    onSuccess = { loginSuccess() }, onFailure = { loginFailed() },
                )
            }
            clearEmailPasswordCharArray(email, password)
        }

    private fun initEmailPasswordCharArray(emailEditable: Editable, passwordEditable: Editable): Pair<CharArray, CharArray> {
        return Pair(initEmailCharArray(emailEditable), initPasswordCharArray(passwordEditable))
    }

    private fun initEmailCharArray(emailEditable: Editable): CharArray {
        val email = CharArray(emailEditable.length)
        emailEditable.trim().forEachIndexed { index, c ->
            email[index] = c
        }
        return email
    }

    private fun initPasswordCharArray(passwordEditable: Editable): CharArray {
        val password = CharArray(passwordEditable.length)
        passwordEditable.trim().forEachIndexed { index, c ->
            password[index] = c
        }
        return password
    }

    private fun loginFailed() {
        setLoginState(LoginState.LoginFailed("로그인 실패"))
    }

    private fun loginSuccess() {
        setLoginState(LoginState.LoginSuccess)
    }

    private fun loginFaledWithRegexError() {
        setLoginState(LoginState.RegexError)
    }
}
