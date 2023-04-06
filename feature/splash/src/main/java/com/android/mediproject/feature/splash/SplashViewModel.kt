package com.android.mediproject.feature.splash

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SplashEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : SplashEvent) = viewModelScope.launch{ _eventFlow.emit(event)}

    init{
        viewModelScope.launch {
            delay(2000)
            event(SplashEvent.TimerDone())
        }
    }

    sealed class SplashEvent{
        data class TimerDone(val unit : Unit? = null) : SplashEvent()
    }
}