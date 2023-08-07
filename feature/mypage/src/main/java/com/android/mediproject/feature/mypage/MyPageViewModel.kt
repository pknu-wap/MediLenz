package com.android.mediproject.feature.mypage

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.domain.SignUseCase
import com.android.mediproject.core.domain.GetUserUseCase
import com.android.mediproject.core.model.comments.MyComment
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.model.user.User
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val signUseCase: SignUseCase,
) : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: MyPageEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun login() = event(MyPageEvent.Login)

    fun signUp() = event(MyPageEvent.SignUp)

    fun navigateToMyPageMore() = event(MyPageEvent.NavigateToMyPageMore)

    fun navigateToMyCommnetList() = event(MyPageEvent.NavigateToMyCommentList)

    sealed class MyPageEvent {
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
        object NavigateToMyPageMore : MyPageEvent()
        object NavigateToMyCommentList : MyPageEvent()
    }

    private val _token = MutableStateFlow<TokenState<CurrentTokens>>(TokenState.Empty)
    val token get() = _token.asStateFlow()

    fun loadTokens() = viewModelScope.launch { getTokenUseCase().collect { _token.value = it } }

    private val _user = MutableStateFlow(User("기본값"))
    val user get() = _user.asStateFlow()

    fun loadUser() = viewModelScope.launch { getUserUseCase().collect { _user.value = it } }

    private val _myCommentsList = MutableSharedFlow<List<MyComment>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val myCommentsList get() = _myCommentsList.asSharedFlow()

    fun loadComments() = viewModelScope.launch { _myCommentsList.emit(dummy) }

    private val _loginMode = MutableStateFlow(LoginMode.GUEST_MODE)
    val loginMode get() = _loginMode.asStateFlow()

    fun setLoginMode(loginMode: LoginMode) {
        _loginMode.value = loginMode
    }

    enum class LoginMode {
        LOGIN_MODE, GUEST_MODE
    }

    fun signOut() = viewModelScope.launch { signUseCase.signOut() }

    val dummy = listOf(
        MyComment(20230528, "타이레놀", "아따 좋습니다 좋아요", System.currentTimeMillis().toString(), 3),
        MyComment(
            20230529,
            "가나다라마바사",
            "이건 실제로 있는 약일까요?",
            System.currentTimeMillis().toString(),
            0,
        ),
        MyComment(
            20230530,
            "코메키나",
            "이건 가까운 약국에서 구할 수 있어요. 하여튼 구할 수 있어요.",
            System.currentTimeMillis().toString(),
            2,
        ),
        MyComment(
            20230528,
            "가나다라마바사",
            "이건 실제로 있는 약일까요?",
            System.currentTimeMillis().toString(),
            0,
        ),
        MyComment(20230531, "아자차카", "이건 확실히 없을 것 같네요.", System.currentTimeMillis().toString(), 2),
    )
}
