package com.android.mediproject.feature.setting

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SettingEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SettingEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun notice(){event(SettingEvent.Notice())}
    fun introduce() = event(SettingEvent.Introduce())
    fun policy() = event(SettingEvent.Policy())
    fun privacy() = event(SettingEvent.Privacy())
    fun communicate() = event(SettingEvent.Communicate())

    sealed class SettingEvent {
        data class Notice(val unit : Unit? = null) : SettingEvent()
        data class Introduce(val unit : Unit? = null) : SettingEvent()
        data class Policy(val unit : Unit? = null) : SettingEvent()
        data class Privacy(val unit : Unit? = null) : SettingEvent()
        data class Communicate(val unit : Unit? = null) : SettingEvent()
    }
}