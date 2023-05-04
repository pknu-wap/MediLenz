package com.android.mediproject.feature.search

import androidx.lifecycle.SavedStateHandle
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchMedicinesViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {
    private val _searchQuerySavedState = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")
    val searchQuery: StateFlow<String> = _searchQuerySavedState

    fun searchMedicines(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

}

private const val SEARCH_QUERY_KEY = "searchQuery"