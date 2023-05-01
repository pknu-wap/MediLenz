package com.android.mediproject.feature.intro

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<LoginEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: LoginEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    sealed class LoginEvent {

    }
}