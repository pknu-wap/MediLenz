package com.android.mediproject.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMedicinesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val getMedicineApprovalListUseCase: GetMedicineApprovalListUseCase
) : BaseViewModel() {
    private val _searchQuerySavedState = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")
    val searchQuery: StateFlow<String> = _searchQuerySavedState

    private val _searchResult = MutableStateFlow<UiState<Flow<PagingData<ApprovedMedicineItemDto>>>>(UiState.isInitial)
    val searchResult = _searchResult.asStateFlow()

    fun searchMedicines(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query

        viewModelScope.launch {
            _searchResult.value = UiState.isLoading
            try {
                val result = getMedicineApprovalListUseCase.invoke(
                    itemName = query,
                    entpName = null,
                ).cachedIn(this@launch)

                _searchResult.value = UiState.Success(result)
            } catch (e: Exception) {
                _searchResult.value = UiState.Error(e.message ?: "데이터 로드 실패")
            }
        }
    }

}

private const val SEARCH_QUERY_KEY = "searchQuery"