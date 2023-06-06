package com.android.mediproject.core.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WindowViewModel @Inject constructor() : ViewModel() {

    private val _bottomNavHeight = MutableStateFlow(0)
    val bottomNavHeight get() = _bottomNavHeight.asStateFlow()

    var bottomNavHeightInPx: Int = 0
        set(value) {
            field = value
            _bottomNavHeight.value = value
        }
}