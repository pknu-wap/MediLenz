package com.android.mediproject.feature.mypage

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.medicine.InterestedMedicine.MedicineInterestedDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val myCommentsList: Flow<List<MyCommentDto>> = channelFlow {

        //dummy for test
        val dummy = listOf(
            MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),3),
            MyCommentDto(20230529,"가나다라마바사","이건 실제로 있는 약일까요?",System.currentTimeMillis().toString(),0),
            MyCommentDto(20230530,"코메키나","이건 가까운 약국에서 구할 수 있어요. 하여튼 구할 수 있어요.",System.currentTimeMillis().toString(),2),
            MyCommentDto(20230528,"가나다라마바사","이건 실제로 있는 약일까요?",System.currentTimeMillis().toString(),0),
            MyCommentDto(20230531,"아자차카","이건 확실히 없을 것 같네요.",System.currentTimeMillis().toString(),2)
        ).sortedBy { it.createdAt }

        send( dummy )
    }

    fun event(event : MyPageEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun login() = event(MyPageEvent.Login)
    fun signUp() = event(MyPageEvent.SignUp)
    fun myPageMore() = event(MyPageEvent.MyPageMore)

    sealed class MyPageEvent{
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
        object MyPageMore : MyPageEvent()
    }
}