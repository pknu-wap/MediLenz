package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class IntroViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<IntroEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : IntroEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun nonMemberLogin(){event(IntroEvent.NonMemberLogin())}
    fun memberLogin(){event(IntroEvent.MemberLogin())}
    fun signUp(){event(IntroEvent.SignUp())}

    sealed class IntroEvent {
        data class NonMemberLogin(val unit : Unit? = null) : IntroEvent()
        data class MemberLogin(val unit : Unit? = null) : IntroEvent()
        data class SignUp(val unit : Unit? = null) : IntroEvent()
    }
}