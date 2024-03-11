package com.android.mediproject.feature.mypage.mypagemore

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.EditUserAccountUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreDialogViewModel @Inject constructor(
    private val editUserAccountUseCase: EditUserAccountUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) :
    BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreDialogEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: MyPageMoreDialogEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun completeDialog() = event(MyPageMoreDialogEvent.CompleteDialog)

    fun cancelDialog() = event(MyPageMoreDialogEvent.CancelDialog)

    sealed class MyPageMoreDialogEvent {
        object CompleteDialog : MyPageMoreDialogEvent()
        object CancelDialog : MyPageMoreDialogEvent()
    }

    private val _myPageDialogState = MutableStateFlow<MyPageDialogState>(MyPageDialogState.initial)
    val myPageMoreDialogState = _myPageDialogState.asStateFlow()

    private fun setMyPageDialogState(myPageDialogState: MyPageDialogState) {
        _myPageDialogState.value = myPageDialogState
    }

    sealed class MyPageDialogState {
        object initial : MyPageDialogState()
        data class Success(val myPageDialogFlag: MyPageDialogFlag) : MyPageDialogState()
        data class Error(val myPageDialogFlag: MyPageDialogFlag) : MyPageDialogState()
    }

    enum class MyPageDialogFlag {
        WITHDRAWAL, CHANGENICKNAME, LOGOUT, CHANGEPASSWORD
    }

    private val _dialogType = MutableStateFlow(MyPageMoreDialogFragment.DialogType.CHANGE_NICKNAME)
    val dialogType = _dialogType.asStateFlow()

    fun setDialogType(dialogType: MyPageMoreDialogFragment.DialogType) {
        _dialogType.value = dialogType
    }

    fun changeNickname(newNickname: String) = viewModelScope.launch(ioDispatcher) {
        /* editUserAccountUseCase.changeNickname(changeNicknameParameter = ChangeNicknameParameter(newNickname))
             .collect {
                 it.fold(
                     onSuccess = { setMyPageDialogState(MyPageDialogState.Success(MyPageDialogFlag.CHANGENICKNAME)) },
                     onFailure = { setMyPageDialogState(MyPageDialogState.Error(MyPageDialogFlag.CHANGENICKNAME)) },
                 )
             }*/
    }

    fun logout() = viewModelScope.launch {
        setMyPageDialogState(MyPageDialogState.Success(MyPageDialogFlag.LOGOUT))
    }

    fun withdrawal() = viewModelScope.launch {
        /*     editUserAccountUseCase.withdrawal().collect {
                 it.fold(
                     onSuccess = {
                         setMyPageDialogState(MyPageDialogState.Success(MyPageDialogFlag.WITHDRAWAL))
                     },
                     onFailure = {
                         setMyPageDialogState(MyPageDialogState.Error(MyPageDialogFlag.WITHDRAWAL))
                     },
                 )
             }*/
    }

    fun changePassword(newPassword: String) = viewModelScope.launch(ioDispatcher) {
        if (!isPasswordValid(newPassword)) {
            setMyPageDialogState(MyPageDialogState.Error(MyPageDialogFlag.CHANGEPASSWORD))
            return@launch
        }

        val password = CharArray(newPassword.length)
        newPassword.trim().forEachIndexed { index, c ->
            password[index] = c
        }

        /*    editUserAccountUseCase.changePassword(changePasswordParameter = ChangePasswordParameter(password))
                .collect {
                    it.fold(
                        onSuccess = { setMyPageDialogState(MyPageDialogState.Success(MyPageDialogFlag.CHANGEPASSWORD)) },
                        onFailure = { setMyPageDialogState(MyPageDialogState.Error(MyPageDialogFlag.CHANGEPASSWORD)) },
                    )
                }*/
    }
}
