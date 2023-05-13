package com.android.mediproject.feature.mypage

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MyPageViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : MyPageEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun myCommentList() = event(MyPageEvent.MyCommentsList())
    fun interestedMedicineList() = event(MyPageEvent.InterestedMedicineList())

    sealed class MyPageEvent{
        data class MyCommentsList(val unit : Unit? = null) : MyPageEvent()
        data class InterestedMedicineList(val unit : Unit? = null) : MyPageEvent()
    }
}