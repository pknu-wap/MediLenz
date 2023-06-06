package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.user.UserUseCase
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreDialogViewModel @Inject constructor(private val userUseCase: UserUseCase) :
    ViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreDialogEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _dialogFlag =
        MutableStateFlow<MyPageMoreDialogFragment.DialogFlag>(MyPageMoreDialogFragment.DialogFlag.ChangeNickName)
    val dialogFlag = _dialogFlag.asStateFlow()

    fun setDialogFlag(dialogFlag: MyPageMoreDialogFragment.DialogFlag) {
        _dialogFlag.value = dialogFlag
    }

    fun event(event: MyPageMoreDialogEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun completeDialog() = event(MyPageMoreDialogEvent.CompleteDialog)
    fun cancelDialog() = event(MyPageMoreDialogEvent.CancelDialog)

    fun changeNickname(newNickname: String) = viewModelScope.launch {
        userUseCase.changeNickname(changeNicknameParameter = ChangeNicknameParameter(newNickname)).collect{

        }
    }

    fun withdrawal() = viewModelScope.launch {
        userUseCase.withdrawal().collect {

        }
    }

    sealed class MyPageMoreDialogEvent {
        object CompleteDialog : MyPageMoreDialogEvent()
        object CancelDialog : MyPageMoreDialogEvent()
    }
}