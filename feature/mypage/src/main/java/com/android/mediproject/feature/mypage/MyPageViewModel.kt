package com.android.mediproject.feature.mypage

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.domain.SignUseCase
import com.android.mediproject.core.domain.GetUserUseCase
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.model.user.User
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val signUseCase: SignUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: MyPageEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(MyPageEvent.Login)

    fun signUp() = event(MyPageEvent.SignUp)

    fun navigateToMyPageMore() = event(MyPageEvent.NavigateToMyPageMore)

    fun navigateToMyCommentList() = event(MyPageEvent.NavigateToMyCommentList)

    sealed class MyPageEvent {
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
        object NavigateToMyPageMore : MyPageEvent()
        object NavigateToMyCommentList : MyPageEvent()
    }

    private val _token = MutableSharedFlow<TokenState<CurrentTokens>>(replay = 1)
    val token get() = _token.asSharedFlow()

    fun loadTokens() = viewModelScope.launch {
        getTokenUseCase().collect {
            _token.emit(it) } }

    private val _user = MutableStateFlow<UiState<User>>(UiState.Init)
    val user get() = _user.asStateFlow()

    fun setUserUiState(uiState: UiState<User>) {
        _user.value = uiState
    }

    fun loadUser() = viewModelScope.launch(ioDispatcher) {
        setUserUiState(UiState.Init)
        getUserUseCase().collectLatest {
            setUserUiState(UiState.Success(it))
        }
    }

    private val _myCommentsList = MutableStateFlow<UiState<List<MyCommentsListResponse.Comment>>>(UiState.Init)
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

    private val _loginMode = MutableStateFlow(LoginMode.Init)
    val loginMode get() = _loginMode.asStateFlow()

    fun setLoginMode(loginMode: LoginMode) {
        _loginMode.value = loginMode
    }

    enum class LoginMode {
        LOGIN_MODE, GUEST_MODE, Init
    }

    fun signOut() = viewModelScope.launch { signUseCase.signOut() }
}
