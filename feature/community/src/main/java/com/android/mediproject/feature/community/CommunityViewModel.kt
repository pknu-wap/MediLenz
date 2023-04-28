package com.android.mediproject.feature.community

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CommunityViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<CommunityEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : CommunityEvent) = viewModelScope.launch{ _eventFlow.emit(event)}

    sealed class CommunityEvent{
    }
}