package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<IntroEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: IntroEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun nonMemberLogin() = event(IntroEvent.NonMemberLogin)
    fun memberLogin() = event(IntroEvent.MemberLogin)
    fun signUp() = event(IntroEvent.SignUp)

    sealed class IntroEvent {
        object NonMemberLogin : IntroEvent()
        object MemberLogin : IntroEvent()
        object SignUp : IntroEvent()
    }
}