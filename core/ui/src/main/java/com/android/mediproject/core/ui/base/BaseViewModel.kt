package com.android.mediproject.core.ui.base

import MutableEventFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import asEventFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _viewBaseEvent = MutableEventFlow<BaseEvent>()
    val viewEvent = _viewBaseEvent.asEventFlow()

    fun backClicked() = event(BaseEvent.Back())
    fun event(event: BaseEvent) = viewModelScope.launch { _viewBaseEvent.emit(event) }

    sealed class BaseEvent {
        data class Back(val unit: Unit? = null) : BaseEvent()
    }
}