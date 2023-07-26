package com.android.mediproject.feature.mypage.mypagemore

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
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

    private val _bottomsheetFlag = MutableStateFlow(MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_NICKNAME)
    val bottomsheetFlag get() = _bottomsheetFlag.asStateFlow()

    fun event(event: MyPageMoreBottomSheetEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun completeBottomSheet() = event(MyPageMoreBottomSheetEvent.CompleteBottomSheet(_bottomsheetFlag.value))

    fun changeNickName() {
        _bottomsheetFlag.value = MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_NICKNAME
    }

    fun changePassword() {
        _bottomsheetFlag.value = MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_PASSWORD
    }

    fun withdrawal() {
        _bottomsheetFlag.value = MyPageMoreBottomSheetFragment.BottomSheetFlag.WITHDRAWAL
    }

    fun logout() {
        _bottomsheetFlag.value = MyPageMoreBottomSheetFragment.BottomSheetFlag.LOGOUT
    }

    sealed class MyPageMoreBottomSheetEvent {
        data class CompleteBottomSheet(val flag: MyPageMoreBottomSheetFragment.BottomSheetFlag = MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_NICKNAME) :
            MyPageMoreBottomSheetEvent()
    }
}
