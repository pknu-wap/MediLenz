package com.android.mediproject.feature.intro.verification

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.bindingadapter.ISendText
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.data.session.AccountSessionRepository
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val signRepository: SignRepository,
    private val accountSessionRepository: AccountSessionRepository,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: kotlinx.coroutines.CoroutineDispatcher,
) : BaseViewModel(), ISendText {

    val email = accountSessionRepository.lastSavedEmail.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, "")

    private val _verificationState = MutableEventFlow<VerificationState>(replay = 1)
    val verificationState = _verificationState.asEventFlow()

    override fun onClickWithText(text: String) {
        viewModelScope.launch {
            withContext(defaultDispatcher) { signRepository.confirmEmail(email.value, text) }.onSuccess {
                _verificationState.emit(VerificationState.Verified)
            }.onFailure {
                _verificationState.emit(VerificationState.VerifyFailed)
            }
        }
    }
}

sealed interface VerificationState {
    data object Verified : VerificationState
    data object VerifyFailed : VerificationState
}
