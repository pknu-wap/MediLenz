package com.android.mediproject.feature.news.recallsuspension

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.domain.GetRecallSuspensionInfoUseCase
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecallSuspensionViewModel @Inject constructor(getRecallSuspensionInfoUseCase: GetRecallSuspensionInfoUseCase) : BaseViewModel() {

    private lateinit var _recallDisposalList: Flow<PagingData<RecallSuspensionListItemDto>>
    val recallDisposalList = _recallDisposalList

    init {
        viewModelScope.launch {
            _recallDisposalList = getRecallSuspensionInfoUseCase.getRecallDisposalList(
            ).let { pager ->
                pager.map { pagingData ->
                    pagingData.map { item ->
                        item.onClick = this@RecallSuspensionViewModel::openDetail
                        item
                    }
                }
            }.cachedIn(viewModelScope)
        }
    }

    private fun openDetail(item: RecallSuspensionListItemDto) {

    }

}