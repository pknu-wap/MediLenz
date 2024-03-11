package com.android.mediproject.feature.mypage

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.data.session.AccountSessionRepository
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.user.UserEntity
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val accountSessionRepository: AccountSessionRepository,
    private val signRepository: SignRepository,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    val currentUser = accountSessionRepository.userOnCurrentSession.map {
        if (it != null) {
            LoginUiState.Online(it)
        } else {
            LoginUiState.Offline()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _myCommentsList = MutableStateFlow<UiState<List<MyCommentsListResponse.Comment>>>(UiState.Initial)
    val myCommentsList = _myCommentsList.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                if (accountSessionRepository.signedIn.not()) {
                    accountSessionRepository.loadSession()
                }
            }
        }
    }

    fun event(event: MyPageEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(MyPageEvent.Login)

    fun signUp() = event(MyPageEvent.SignUp)

    fun signOut() = viewModelScope.launch {
        withContext(ioDispatcher) {
            signRepository.logout()
        }
    }

    fun navigateToMyPageMore() = event(MyPageEvent.NavigateToMyPageMore)

    fun navigateToMyCommentList() = event(MyPageEvent.NavigateToMyCommentList)


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

sealed interface MyPageEvent {
    data object Login : MyPageEvent
    data object SignUp : MyPageEvent
    data object NavigateToMyPageMore : MyPageEvent
    data object NavigateToMyCommentList : MyPageEvent
}


sealed interface LoginUiState {
    val userEntity: UserEntity

    class Online(override val userEntity: UserEntity) : LoginUiState
    class Offline(override val userEntity: UserEntity = UserEntity()) : LoginUiState
}
