package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetSkippableIntroUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getSkippableIntroUseCase: GetSkippableIntroUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<IntroEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getSkippableIntroUseCase().collectLatest {
                if (it) event(IntroEvent.SkipIntro)
                else event(IntroEvent.NonSkipIntro)
            }
        }
    }

    fun event(event: IntroEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }


    fun nonMemberLogin() = event(IntroEvent.NonMemberLogin())
    fun memberLogin() = event(IntroEvent.MemberLogin())
    fun signUp() = event(IntroEvent.SignUp())

    sealed class IntroEvent {
        data class NonMemberLogin(val unit: Unit? = null) : IntroEvent()
        data class MemberLogin(val unit: Unit? = null) : IntroEvent()
        data class SignUp(val unit: Unit? = null) : IntroEvent()
        object SkipIntro : IntroEvent()
        object NonSkipIntro : IntroEvent()
    }
}