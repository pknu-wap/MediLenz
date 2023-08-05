package com.android.mediproject.feature.setting

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SettingEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SettingEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun notice() = event(SettingEvent.Notice)
    fun introduce() = event(SettingEvent.Introduce)
    fun policy() = event(SettingEvent.Policy)
    fun privacy() = event(SettingEvent.Privacy)
    fun communicate() = event(SettingEvent.Communicate)

    sealed class SettingEvent {
        object Notice : SettingEvent()
        object Introduce : SettingEvent()
        object Policy : SettingEvent()
        object Privacy : SettingEvent()
        object Communicate : SettingEvent()
    }
}