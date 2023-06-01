package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.ViewModel
import asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MyPageMoreDialogViewModel : ViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreDialogEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    sealed class MyPageMoreDialogEvent{

    }
}