package com.android.mediproject.feature.home

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<HomeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _logoColor = MutableStateFlow<LogoColor>(LogoColor.Main)
    val logoColor = _logoColor.asStateFlow()

    fun setLogoColor(logoColor: LogoColor) {
        viewModelScope.launch { _logoColor.value = logoColor }
    }

    fun event(event: HomeEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToSearch() = event(HomeEvent.NavigateToSearch)

    sealed class HomeEvent {
        object NavigateToSearch : HomeEvent()
    }

    sealed interface LogoColor {
        object White : LogoColor
        object Main : LogoColor
    }
}
