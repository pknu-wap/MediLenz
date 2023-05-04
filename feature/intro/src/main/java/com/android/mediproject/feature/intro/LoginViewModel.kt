package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<LoginEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: LoginEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(LoginEvent.Login())
    fun signUp() = event(LoginEvent.SignUp())

    sealed class LoginEvent {
        data class Login(val unit: Unit? = null) : LoginEvent()
        data class SignUp(val unit: Unit? = null) : LoginEvent()
    }
}