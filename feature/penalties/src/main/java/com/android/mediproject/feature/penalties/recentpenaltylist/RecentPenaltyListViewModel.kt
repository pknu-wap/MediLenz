package com.android.mediproject.feature.penalties.recentpenaltylist

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetRecallSuspensionInfoUseCase
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentPenaltyListViewModel @Inject constructor(getRecallSuspensionInfoUseCase: GetRecallSuspensionInfoUseCase) :
    BaseViewModel() {

    private val _recallDisposalList =
        MutableStateFlow<UiState<List<RecallSuspensionListItemDto>>>(UiState.Initial)
    val recallDisposalList get() = _recallDisposalList.asStateFlow()

    /**
     * 회수 폐기 공고 목록을 로드
     */
    init {
        viewModelScope.launch {
            getRecallSuspensionInfoUseCase.getRecentRecallDisposalList(numOfRows = 5)
                .fold(onSuccess = {
                    _recallDisposalList.value = UiState.Success(it)
                }, onFailure = {
                    _recallDisposalList.value = UiState.Error(it.message ?: "failed")
                })
        }
    }

}