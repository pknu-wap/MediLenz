package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.mypage.MyPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreBottomSheetViewModel @Inject constructor() : BaseViewModel() {

    companion object{
        const val CHANGE_NICKNAME = 301
        const val CHANGE_PASSWORD = 302
        const val WITHDRAWAL = 303
    }

    private val _eventFlow = MutableEventFlow<MyPageMoreBottomSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _bottomsheetFlag = MutableStateFlow(CHANGE_NICKNAME)
    val bottomsheetFlag get() = _bottomsheetFlag.asStateFlow()

    fun event(event : MyPageMoreBottomSheetEvent) = viewModelScope.launch{ _eventFlow.emit(event)}
    fun confirm() = event(MyPageMoreBottomSheetEvent.Confirm(_bottomsheetFlag.value))
    fun changeNickName(){ _bottomsheetFlag.value = CHANGE_NICKNAME }
    fun changePassword(){ _bottomsheetFlag.value = CHANGE_PASSWORD }
    fun withdrawal(){ _bottomsheetFlag.value = WITHDRAWAL }

    sealed class MyPageMoreBottomSheetEvent{
        data class Confirm(val flag : Int? = CHANGE_NICKNAME) : MyPageMoreBottomSheetEvent()
    }
}