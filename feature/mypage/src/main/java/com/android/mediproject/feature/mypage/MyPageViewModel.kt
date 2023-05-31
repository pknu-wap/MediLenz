package com.android.mediproject.feature.mypage

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.domain.sign.GetUserUseCase
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.user.UserDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel() {

    val dummy = listOf(
        MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),3),
        MyCommentDto(20230529,"가나다라마바사","이건 실제로 있는 약일까요?",System.currentTimeMillis().toString(),0),
        MyCommentDto(20230530,"코메키나","이건 가까운 약국에서 구할 수 있어요. 하여튼 구할 수 있어요.",System.currentTimeMillis().toString(),2),
        MyCommentDto(20230528,"가나다라마바사","이건 실제로 있는 약일까요?",System.currentTimeMillis().toString(),0),
        MyCommentDto(20230531,"아자차카","이건 확실히 없을 것 같네요.",System.currentTimeMillis().toString(),2)
    ).sortedBy { it.createdAt }

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _token = MutableStateFlow<TokenState<CurrentTokenDto>>(TokenState.Empty)
    val token get() = _token.asStateFlow()

    private val _user = MutableStateFlow(UserDto("기본값"))
    val user: StateFlow<UserDto>
        get() = _user.asStateFlow()

    private val _myCommentsList = MutableStateFlow(dummy)
    val myCommentsList get() = _myCommentsList.asStateFlow()

    fun loadTokens() = viewModelScope.launch { getTokenUseCase().collect { _token.value = it } }
    fun loadUser() = viewModelScope.launch { getUserUseCase().collect { _user.value = it } }
    fun loadComments() = viewModelScope.launch{ _myCommentsList.value = dummy }

    fun event(event: MyPageEvent) = viewModelScope.launch { _eventFlow.emit(event) }
    fun login() = event(MyPageEvent.Login)
    fun signUp() = event(MyPageEvent.SignUp)
    fun myPageMore() = event(MyPageEvent.MyPageMore)

    sealed class MyPageEvent {
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
        object MyPageMore : MyPageEvent()
    }
}