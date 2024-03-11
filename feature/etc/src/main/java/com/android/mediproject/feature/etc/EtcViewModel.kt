package com.android.mediproject.feature.etc

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EtcViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<EtcEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: EtcEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun notice() = event(EtcEvent.Notice)
    fun introduce() = event(EtcEvent.Introduce)
    fun policy() = event(EtcEvent.Policy)
    fun privacy() = event(EtcEvent.Privacy)
    fun communicate() = event(EtcEvent.Communicate)

    sealed class EtcEvent {
        data object Notice : EtcEvent()
        data object Introduce : EtcEvent()
        data object Policy : EtcEvent()
        data object Privacy : EtcEvent()
        data object Communicate : EtcEvent()
    }
}
