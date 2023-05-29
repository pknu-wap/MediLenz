package com.android.mediproject.feature.search.recentsearchlist

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.SearchHistoryUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class RecentSearchListViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {
    val searchHistoryList by lazy {
        suspend {
            searchHistoryUseCase.getSearchHistoryList(6).distinctUntilChanged().shareIn(
                viewModelScope, started = SharingStarted.Lazily, replay = 1
            )
        }
    }
}