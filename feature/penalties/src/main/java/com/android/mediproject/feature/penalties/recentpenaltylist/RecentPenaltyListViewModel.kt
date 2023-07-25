package com.android.mediproject.feature.penalties.recentpenaltylist

import MutableEventFlow
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetRecallSuspensionInfoUseCase
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentPenaltyListViewModel @Inject constructor(
    private val getRecallSuspensionInfoUseCase: GetRecallSuspensionInfoUseCase) : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<PenaltyListEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: PenaltyListEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToNews() = event(PenaltyListEvent.NavigateToNews)

    sealed class PenaltyListEvent {
        object NavigateToNews : PenaltyListEvent()
    }

    private val _recallDisposalList = MutableStateFlow<UiState<List<RecallSuspensionListItemDto>>>(UiState.Initial)
    val recallDisposalList get() = _recallDisposalList.asStateFlow()

    init {
        viewModelScope.launch {
            getRecallSuspensionInfoUseCase.getRecentRecallDisposalList(numOfRows = 5).fold(onSuccess = {
                _recallDisposalList.value = UiState.Success(it)
            }, onFailure = {
                _recallDisposalList.value = UiState.Error(it.message ?: "failed")
            })
        }
    }

}
