package com.android.mediproject.feature.search.recentsearchlist

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.SearchHistoryUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecentSearchListViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
) : BaseViewModel() {
    val searchHistoryList by lazy {
        suspend {
            searchHistoryUseCase.getSearchHistoryList(6).flatMapLatest {
                flowOf(UiState.Success(it))
            }.catch {
                flowOf(UiState.Error(it.message ?: "Error"))
            }.stateIn(viewModelScope)
        }
    }
}