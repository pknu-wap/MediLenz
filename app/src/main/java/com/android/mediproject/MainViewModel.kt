package com.android.mediproject

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MainEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : MainEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun aicamera() = event(MainEvent.AICamera())

    sealed class MainEvent{
        data class AICamera(val unit : Unit? = null) : MainEvent()
    }
}