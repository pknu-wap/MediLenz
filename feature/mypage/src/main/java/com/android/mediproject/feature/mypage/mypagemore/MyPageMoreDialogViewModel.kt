package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageMoreDialogViewModel : ViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreDialogEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _dialogFlag = MutableStateFlow<MyPageMoreDialogFragment.DialogFlag>(MyPageMoreDialogFragment.DialogFlag.ChangeNickName)
    val dialogFlag = _dialogFlag.asStateFlow()

    fun setDialogFlag(dialogFlag : MyPageMoreDialogFragment.DialogFlag){ _dialogFlag.value = dialogFlag}

    fun event(event : MyPageMoreDialogEvent) = viewModelScope.launch{ _eventFlow.emit(event) }

    sealed class MyPageMoreDialogEvent{
    }
}