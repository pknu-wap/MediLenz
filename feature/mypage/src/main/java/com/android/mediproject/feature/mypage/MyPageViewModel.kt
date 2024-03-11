package com.android.mediproject.feature.mypage

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.domain.GetUserUseCase
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.model.user.UserEntity
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val signUseCase: SignRepository,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: MyPageEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(MyPageEvent.Login)

    fun signUp() = event(MyPageEvent.SignUp)

    fun signOut() = viewModelScope.launch {
        signUseCase.signOut()
        loadTokens()
    }

    fun navigateToMyPageMore() = event(MyPageEvent.NavigateToMyPageMore)

    fun navigateToMyCommentList() = event(MyPageEvent.NavigateToMyCommentList)

    sealed class MyPageEvent {
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
        object NavigateToMyPageMore : MyPageEvent()
        object NavigateToMyCommentList : MyPageEvent()
    }

    private val _token = MutableSharedFlow<TokenState<CurrentTokens>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val token get() = _token.asSharedFlow()

    fun loadTokens() = viewModelScope.launch {
        /*      getTokenUseCase().collect {
                  _token.emit(it)
              }*/
    }

    private val _userEntity = MutableStateFlow<UiState<UserEntity>>(UiState.Initial)
    val user get() = _userEntity.asStateFlow()

    fun setUserUiState(uiState: UiState<UserEntity>) {
        _userEntity.value = uiState
    }

    fun loadUser() = viewModelScope.launch(ioDispatcher) {
        setUserUiState(UiState.Initial)
        getUserUseCase().collectLatest {
            setUserUiState(UiState.Success(it))
        }
    }

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
