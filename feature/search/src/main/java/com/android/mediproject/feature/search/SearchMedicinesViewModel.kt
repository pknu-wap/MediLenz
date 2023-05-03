package com.android.mediproject.feature.search

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMedicinesViewModel @Inject constructor() : BaseViewModel() {
    private val _searchWord = MutableSharedFlow<String>(replay = 1)
    val searchWord: SharedFlow<String> = _searchWord

    fun searchMedicines(query: String) {
        viewModelScope.launch {
            _searchWord.emit(query)
            
        }
    }

}