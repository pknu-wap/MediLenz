package com.android.mediproject.feature.etc

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class EtcViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<EtcEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: EtcEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun notice() = event(EtcEvent.Notice)
    fun introduce() = event(EtcEvent.Introduce)
    fun policy() = event(EtcEvent.Policy)
    fun privacy() = event(EtcEvent.Privacy)
    fun communicate() = event(EtcEvent.Communicate)

    sealed class EtcEvent {
        object Notice : EtcEvent()
        object Introduce : EtcEvent()
        object Policy : EtcEvent()
        object Privacy : EtcEvent()
        object Communicate : EtcEvent()
    }
}