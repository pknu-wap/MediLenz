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
        send(
            listOf(
               MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),3),
                MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),0),
                MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),2),
                MyCommentDto(20230528,"타이레놀","아따 좋습니다 좋아요",System.currentTimeMillis().toString(),1)
            ).sortedBy { it.createdAt }
        )
    }

    fun event(event : MyPageEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun login() = event(MyPageEvent.Login)
    fun signUp() = event(MyPageEvent.SignUp)

    sealed class MyPageEvent{
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
    }
}