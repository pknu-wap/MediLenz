package com.android.mediproject.feature.comments.mycommentslist

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCommentsListViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyCommentsListEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: MyCommentsListEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    sealed class MyCommentsListEvent

    private val _myCommentsList = MutableStateFlow<UiState<List<MyCommentsListResponse.Comment>>>(UiState.Initial)
    val myCommentsList get() = _myCommentsList.asStateFlow()

    fun setMyCommentsListUiState(uiState: UiState<List<MyCommentsListResponse.Comment>>) {
        _myCommentsList.value = uiState
    }

    fun loadMyCommentsList() = viewModelScope.launch(ioDispatcher) {
        setMyCommentsListUiState(UiState.Loading)
        getCommentsUseCase.getMyCommentsList().collectLatest { result ->
            result.fold(
                onSuccess = { setMyCommentsListUiState(UiState.Success(it.commentList)) },
                onFailure = { setMyCommentsListUiState(UiState.Error("댓글을 불러오는데 실패하였습니다.")) },
            )
        }
    }
}
