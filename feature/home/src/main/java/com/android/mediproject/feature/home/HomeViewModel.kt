package com.android.mediproject.feature.home

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<HomeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : HomeEvent) = viewModelScope.launch{ _eventFlow.emit(event)}

    sealed class HomeEvent{

    }
}