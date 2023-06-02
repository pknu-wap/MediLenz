package com.android.mediproject.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WindowViewModel @Inject constructor() : ViewModel() {

    private val _bottomNavHeight = MutableStateFlow(-1)
    val bottomNavHeight get() = _bottomNavHeight.asStateFlow()

    fun setBottomNavHeight(height: Int) {
        viewModelScope.launch {
            _bottomNavHeight.value = height
        }
    }
}