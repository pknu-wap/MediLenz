package com.android.mediproject.feature.search.recentsearchlist

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.domain.SearchHistoryUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class RecentSearchListViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase
) : BaseViewModel() {
    val searchHistoryList by lazy {
        suspend {
            searchHistoryUseCase.getSearchHistoryList(6).shareIn(
                viewModelScope, started = SharingStarted.Lazily, replay = 1
            )
        }
    }
}