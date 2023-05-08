package com.android.mediproject.feature.news.recaldisposal

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.domain.GetRecallDisposalListUseCase
import com.android.mediproject.core.model.remote.recalldisposal.RecallDisposalResponse
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RecallDisposalViewModel @Inject constructor(getRecallDisposalListUseCase: GetRecallDisposalListUseCase) : BaseViewModel() {

    val recallDisposalList = getRecallDisposalListUseCase.invoke(
    ).let { pager ->
        pager.map { pagingData ->
            pagingData.map { item ->
                item.onClick = this@RecallDisposalViewModel::openDetail
                item
            }
        }
    }.cachedIn(viewModelScope)

    private fun openDetail(item: RecallDisposalResponse) {

    }

}