package com.android.mediproject.feature.search

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMedicinesViewModel @Inject constructor(

) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery = _searchQuery.asStateFlow()

    fun searchMedicines(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }
}