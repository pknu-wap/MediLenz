package com.android.mediproject.feature.intro

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
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
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    init {
        checkSkipIntro()
    }

    private fun checkSkipIntro() {
        viewModelScope.launch(ioDispatcher) {
            getSkippableIntroUseCase().collectLatest { canSkip ->
                if (canSkip) skipIntro()
                else showIntro()
            }
        }
    }

    fun skipIntro() = event(IntroEvent.SkipIntro)

    fun showIntro() = event(IntroEvent.ShowIntro)

    private val _eventFlow = MutableEventFlow<IntroEvent>(replay = 1)
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: IntroEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    fun nonMemberLogin() = event(IntroEvent.GuestLogin)

    fun memberLogin() = event(IntroEvent.MemberLogin)

    fun signUp() = event(IntroEvent.SignUp)

    sealed class IntroEvent {
        object GuestLogin : IntroEvent()
        object MemberLogin : IntroEvent()
        object SignUp : IntroEvent()
        object SkipIntro : IntroEvent()
        object ShowIntro : IntroEvent()
    }
}
