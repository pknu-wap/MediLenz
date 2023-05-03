package com.android.mediproject.feature.search

import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchMedicinesViewModel @Inject constructor() : BaseViewModel() {

    private val _searchWord = MutableStateFlow("")
    val searchWord
        get() = _searchWord.asStateFlow()

    private val _goToManualSearchResult = MutableStateFlow(false)
    val goToManualSearchResult
        get() = _goToManualSearchResult.asStateFlow()

    /**
     * 검색어와 검색 결과 화면으로 진입할지 여부를 초기화합니다.
     */
    fun init(searchWord: String?, goToManualSearchResult: Boolean) {
        searchWord?.let {
            _searchWord.value = it
        }
        _goToManualSearchResult.value = goToManualSearchResult
    }
}