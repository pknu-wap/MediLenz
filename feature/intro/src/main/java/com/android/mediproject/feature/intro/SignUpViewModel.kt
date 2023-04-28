package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SignUpEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    sealed class SignUpEvent {

    }
}