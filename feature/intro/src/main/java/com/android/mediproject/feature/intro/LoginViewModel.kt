package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUseCase: SignUseCase
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SignEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SignEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(SignEvent.SignIn())
    fun signUp() = event(SignEvent.SignUp())

    fun signIn(signInParameter: SignInParameter) {

    }

    fun signUp(signUpParameter: SignUpParameter) {

    }

    sealed class SignEvent {
        data class SignIn(val sign: Unit? = null) : SignEvent()
        data class SignUp(val unit: Unit? = null) : SignEvent()
    }
}