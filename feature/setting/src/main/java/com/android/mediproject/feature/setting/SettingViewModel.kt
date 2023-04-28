package com.android.mediproject.feature.setting

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SettingEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : SettingEvent) = viewModelScope.launch{ _eventFlow.emit(event)}

    sealed class SettingEvent{

    }
}