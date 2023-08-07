package com.android.mediproject.feature.mypage.mypagemore

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import android.text.Editable
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.core.domain.user.UserUseCase
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreDialogViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) :
    BaseViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageMoreDialogEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: MyPageMoreDialogEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun completeDialog() = event(MyPageMoreDialogEvent.CompleteDialog)

    fun toast(message: String) = event(MyPageMoreDialogEvent.Toast(message))

    fun cancelDialog() = event(MyPageMoreDialogEvent.CancelDialog)

    sealed class MyPageMoreDialogEvent {
        object CompleteDialog : MyPageMoreDialogEvent()
        object CancelDialog : MyPageMoreDialogEvent()
        object CompleteWithdrawal : MyPageMoreDialogEvent()
        object CompleteChangeNickname : MyPageMoreDialogEvent()
        object CompleteLogout : MyPageMoreDialogEvent()
        data class Toast(val message: String) : MyPageMoreDialogEvent()
    }

    private val _myPageMoreDialogState = MutableStateFlow<MyPageMoreDialogState>(MyPageMoreDialogState.initial)
    val myPageMoreDialogState = _myPageMoreDialogState.asStateFlow()

    private fun setMyPageMoreDialogState(myPageMoreDialogState: MyPageMoreDialogState) {
        _myPageMoreDialogState.value = myPageMoreDialogState
    }

    fun completeWithdrawal() = setMyPageMoreDialogState(MyPageMoreDialogState.Complete(MyPageMoreCompleteFlag.WITHDRAWAL))

    fun completeChangeNickname() = setMyPageMoreDialogState(MyPageMoreDialogState.Complete(MyPageMoreCompleteFlag.CHANGENICKNAME))

    fun completeLogout() = setMyPageMoreDialogState(MyPageMoreDialogState.Complete(MyPageMoreCompleteFlag.LOGOUT))


    sealed class MyPageMoreDialogState {
        data class Complete(val myPageMoreCompleteFlag: MyPageMoreCompleteFlag? = null) : MyPageMoreDialogState()
        object initial : MyPageMoreDialogState()
        object PasswordError : MyPageMoreDialogState()
    }

    enum class MyPageMoreCompleteFlag {
        WITHDRAWAL, CHANGENICKNAME, LOGOUT
    }

    private val _dialogFlag = MutableStateFlow(MyPageMoreDialogFragment.DialogFlag.CHANGE_NICKNAME)
    val dialogFlag = _dialogFlag.asStateFlow()

    fun setDialogFlag(dialogFlag: MyPageMoreDialogFragment.DialogFlag) {
        _dialogFlag.value = dialogFlag
    }

    fun changeNickname(newNickname: String) = viewModelScope.launch(ioDispatcher) {
        userUseCase.changeNickname(changeNicknameParameter = ChangeNicknameParameter(newNickname))
            .collect {
                it.fold(
                    onSuccess = { toast("닉네임 변경이 완료되었습니다.") },
                    onFailure = { toast("닉네임 변경에 실패하였습니다.") },
                )
            }
        completeChangeNickname()
        cancelDialog()
    }

    fun logout() = viewModelScope.launch {
        completeLogout()
        toast("로그아웃이 완료되었습니다.")
        cancelDialog()
    }

    fun withdrawal() = viewModelScope.launch {
        userUseCase.withdrawal().collect {
            it.fold(
                onSuccess = {
                    toast("회원 탈퇴가 완료되었습니다.")
                    completeWithdrawal()
                },
                onFailure = {
                    toast("회원 탈퇴에 실패하였습니다.")
                },
            )
        }
        cancelDialog()
    }

    fun changePassword(newPassword: Editable) = viewModelScope.launch(ioDispatcher) {
        if (isPasswordValid(newPassword)) {
            toast("비밀번호는 영어 + 숫자로 이루어진 4~16자로 설정해주세요.")
            cancelDialog()
            return@launch
        }

        val password = CharArray(newPassword.length)
        newPassword.trim().forEachIndexed { index, c ->
            password[index] = c
        }

        userUseCase.changePassword(changePasswordParamter = ChangePasswordParamter(password))
            .collect {
                it.fold(
                    onSuccess = { toast("비밀번호 변경에 성공하였습니다.") },
                    onFailure = { toast("비밀번호 변경에 실패하였습니다.") },
                )
            }
        cancelDialog()
    }
}
