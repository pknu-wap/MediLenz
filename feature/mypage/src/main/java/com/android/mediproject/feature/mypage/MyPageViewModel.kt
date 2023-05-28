package com.android.mediproject.feature.mypage

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : MyPageEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun myCommentList() = event(MyPageEvent.MyCommentsList)
    fun interestedMedicineList() = event(MyPageEvent.InterestedMedicineList)
    fun login() = event(MyPageEvent.Login)
    fun signUp() = event(MyPageEvent.SignUp)

    sealed class MyPageEvent{
        object MyCommentsList : MyPageEvent()
        object InterestedMedicineList : MyPageEvent()
        object Login : MyPageEvent()
        object SignUp : MyPageEvent()
    }
}