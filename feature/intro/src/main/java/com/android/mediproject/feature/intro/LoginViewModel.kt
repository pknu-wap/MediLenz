package com.android.mediproject.feature.intro

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isEmailValid
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.SignUseCase
import com.android.mediproject.core.model.navargs.TOHOME
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUseCase: SignUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    val savedEmail = signUseCase.savedEmail.flatMapLatest {
        if (it.isEmpty()) emptyFlow()
        else flowOf(it.toCharArray())
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = CharArray(0))

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState = _loginState.asStateFlow()

    private fun setLoginState(state: LoginState) {
        _loginState.value = state
    }

    sealed class LoginState {
        data object Initial : LoginState()
        data object Logining : LoginState()
        data object NotVerified : LoginState()
        data object RegexError : LoginState()
        data object LoginSuccess : LoginState()
        data class LoginFailed(val message: String) : LoginState()
    }

    private val _callBackMoveFlag = MutableStateFlow(TOHOME)
    val callBackMoveFlag = _callBackMoveFlag.asStateFlow()

    fun setMoveFlag(flag: Int) {
        _callBackMoveFlag.value = flag
    }

    private val _eventFlow = MutableEventFlow<LoginEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: LoginEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun loginWithCheckRegex() = event(LoginEvent.Login)

    fun signUp() = event(LoginEvent.SignUp)

    sealed class LoginEvent {
        data object Login : LoginEvent()
        data object SignUp : LoginEvent()
    }

    fun loginWithCheckRegex(email: String, password: String, isEmailSaved: Boolean) {
        if (!checkEmailPasswordRegex(email, password)) {
            loginFailedWithRegexError()
            return
        }
        login(email, password, isEmailSaved)
    }

    private fun clearEmailAndPassword(email: CharArray, password: CharArray) {
        email.fill('\u0000')
        password.fill('\u0000')
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

    private fun login(email: String, password: String, isEmailSaved: Boolean) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            loginFailed()
        }
        viewModelScope.launch(exceptionHandler) {
            setLoginState(LoginState.Logining)

            val pw = password.trim().toByteArray()
            val result = withContext(defaultDispatcher) {
                signUseCase.login(LoginParameter(email, pw, isEmailSaved))
            }

            if (result.isSuccess) {
                loginSuccess()
                pw.fill(0)
            } else {
                loginFailed()
            }/*clearEmailAndPassword(emailCharArray, passwordCharArray)*/
        }
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

    private fun initPassword(password: String): ByteArray = password.trim().toByteArray()

    private fun loginFailed() {
        setLoginState(LoginState.LoginFailed("로그인 실패"))
    }

    private fun loginSuccess() {
        setLoginState(LoginState.LoginSuccess)
    }

    private fun loginFailedWithRegexError() {
        setLoginState(LoginState.RegexError)
    }
}
