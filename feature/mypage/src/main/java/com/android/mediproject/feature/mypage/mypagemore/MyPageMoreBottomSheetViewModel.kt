package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.CHANGE_NICKNAME_BOTTOMSHEET
import com.android.mediproject.core.common.CHANGE_PASSWORD_BOTTOMSHEET
import com.android.mediproject.core.common.WITHDRAWAL_BOTTOMSHEET
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreBottomSheetViewModel @Inject constructor() : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreBottomSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _bottomsheetFlag = MutableStateFlow(CHANGE_NICKNAME_BOTTOMSHEET)
    val bottomsheetFlag get() = _bottomsheetFlag.asStateFlow()

    fun event(event : MyPageMoreBottomSheetEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun confirm() = event(MyPageMoreBottomSheetEvent.Confirm(_bottomsheetFlag.value))
    fun changeNickName(){ _bottomsheetFlag.value = CHANGE_NICKNAME_BOTTOMSHEET }
    fun changePassword(){ _bottomsheetFlag.value = CHANGE_PASSWORD_BOTTOMSHEET }
    fun withdrawal(){ _bottomsheetFlag.value = WITHDRAWAL_BOTTOMSHEET }

    sealed class MyPageMoreBottomSheetEvent{
        data class Confirm(val flag : Int? = CHANGE_NICKNAME_BOTTOMSHEET) : MyPageMoreBottomSheetEvent()
    }
}