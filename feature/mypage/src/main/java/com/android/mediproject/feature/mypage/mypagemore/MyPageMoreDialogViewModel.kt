package com.android.mediproject.feature.mypage.mypagemore

import MutableEventFlow
import android.text.Editable
import androidx.lifecycle.viewModelScope
import asEventFlow
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageMoreDialogViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) :
    BaseViewModel() {

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
    fun toast(message: String) = event(MyPageMoreDialogEvent.Toast(message))
    fun withdrawalComplete() = event(MyPageMoreDialogEvent.WithdrawalComplete)
    fun changeNicknameComplete() = event(MyPageMoreDialogEvent.ChangeNicknameComplete)

    fun changeNickname(newNickname: String) = viewModelScope.launch(ioDispatcher) {
        userUseCase.changeNickname(changeNicknameParameter = ChangeNicknameParameter(newNickname))
            .collect {
                it.fold(
                    onSuccess = { toast("닉네임 변경이 완료되었습니다.") },
                    onFailure = { toast("닉네임 변경에 실패하였습니다.") })
            }
        changeNicknameComplete()
        cancelDialog()
    }

    fun withdrawal(withdrawalInput: String) = viewModelScope.launch {
        userUseCase.withdrawal().collect {
            it.fold(onSuccess = {
                toast("회원 탈퇴가 완료되었습니다.")
                withdrawalComplete()
            }, onFailure = {
                toast("회원 탈퇴에 실패하였습니다.")
            })
        }
        cancelDialog()
    }

    fun changePassword(newPassword: Editable) = viewModelScope.launch(ioDispatcher) {
        if (isPasswordValid(newPassword)) {
            toast("비밀번호는 영어 + 숫자로 이루어진 4~10자로 설정해주세요.")
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
                    onFailure = { toast("비밀번호 변경에 실패하였습니다.") }
                )
            }
        cancelDialog()
    }

    sealed class MyPageMoreDialogEvent {
        object CompleteDialog : MyPageMoreDialogEvent()
        object CancelDialog : MyPageMoreDialogEvent()
        object WithdrawalComplete : MyPageMoreDialogEvent()
        object ChangeNicknameComplete : MyPageMoreDialogEvent()
        data class Toast(val message: String) : MyPageMoreDialogEvent()
    }
}