package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.mypage.MyPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreBottomSheetViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageMoreBottomSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event : MyPageMoreBottomSheetEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun confirm() = event(MyPageMoreBottomSheetEvent.Confirm)

    sealed class MyPageMoreBottomSheetEvent{
        object Confirm : MyPageMoreBottomSheetEvent()
    }
}