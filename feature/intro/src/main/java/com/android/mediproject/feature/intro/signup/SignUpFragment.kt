package com.android.mediproject.feature.intro.signup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.delayTextChangedCallback
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.navargs.TOHOME
import com.android.mediproject.core.model.navargs.TOMYPAGE
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.R
import com.android.mediproject.feature.intro.databinding.FragmentSignUpBinding
import com.android.mediproject.feature.intro.verification.EmailVerficationDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>(FragmentSignUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()

    @Inject @Dispatcher(MediDispatchers.Default) lateinit var defaultDispatcher: CoroutineDispatcher

    @Inject lateinit var systemBarStyler: SystemBarStyler

    private val mainScope = MainScope()

    private val jobs = mutableListOf<Job>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted { eventFlow.collectLatest { handleEvent(it) } }
                repeatOnStarted { signUpUiState.collectLatest { handleSignUpState(it) } }
            }
        }
        setDelayTextWatcher()
        setBarStyle()
        setCallBackMoveFlag()
        setSignUpButtonDisenabled()
    }

    private fun handleEvent(event: SignUpViewModel.SignUpEvent) {
        when (event) {
            is SignUpViewModel.SignUpEvent.SignUp -> signUp()
        }
    }

    private fun signUp() {
        fragmentViewModel.signUpWithCheckRegex(
            binding.signUpEmail.getValue(),
            binding.signUpPassword.getValue(),
            binding.signUpPasswordCheck.getValue(),
            binding.signUpNickName.getValue(),
        )
    }

    private fun handleSignUpState(signUpUiState: SignUpUiState) {
        when (signUpUiState) {
            is SignUpUiState.SigningUp -> signingUp()
            is SignUpUiState.Success -> signUpSuccess()
            is SignUpUiState.Failed -> toast(signUpUiState.message)
            is SignUpUiState.RegexError -> toast(signUpUiState.text)
            is SignUpUiState.UserExists -> toast(signUpUiState.text)
            is SignUpUiState.PasswordError -> toast(signUpUiState.text)
        }
    }

    private fun signingUp() {
        LoadingDialog.showLoadingDialog(
            requireActivity(),
            getString(R.string.signingUp),
        )
    }

    private fun signUpSuccess() {
        LoadingDialog.dismiss()
        toast(getString(R.string.signUpSuccess))
        EmailVerficationDialogFragment().show(childFragmentManager, "EmailVerificationDialogFragment")
        //handleCallBackMoveFlag()
    }

    private fun handleCallBackMoveFlag() {
        when (getCallBackMoveFlag()) {
            TOHOME -> navigateToHome()
            TOMYPAGE -> navigateToMyPage()
        }
    }

    private fun getCallBackMoveFlag(): Int {
        return fragmentViewModel.callBackMoveFlag.value
    }

    private fun navigateToHome() {
        navigateWithUriNavOptions(
            "medilens://main/home_nav",
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build(),
        )
    }

    private fun navigateToMyPage() {
        navigateWithUriNavOptions(
            "medilens://main/mypage_nav",
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build(),
        )
    }

    private fun signUpFailed() {
        LoadingDialog.dismiss()
        toast(getString(R.string.signUpFailed))
    }

    private fun regexError() {
        toast(getString(R.string.signInRegexError))
    }

    private fun initial() {
    }

    private fun passwordError() {
        toast(getString(R.string.signUpPasswordError))
    }

    private fun setDelayTextWatcher() = binding.apply {
        addDelayTextWatcher(signUpEmail.inputData)
        addDelayTextWatcher(signUpPassword.inputData)
        addDelayTextWatcher(signUpPasswordCheck.inputData)
        addDelayTextWatcher(signUpNickName.inputData)
    }

    private fun addDelayTextWatcher(editText: EditText) {
        mainScope.launch(
            context = defaultDispatcher + Job().apply {
                jobs.add(this)
            },
        ) {
            editText.delayTextChangedCallback().debounce(500L).onEach { seq ->
                mainScope.launch {
                    binding.signUpComplete.isEnabled = !seq.isNullOrEmpty()
                }
            }.launchIn(this)
        }
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    signUpBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }

    private fun setCallBackMoveFlag() {
        val moveFlag = arguments?.getInt("flag", TOHOME)
        fragmentViewModel.setCallBackMoveFlag(moveFlag ?: TOHOME)
    }

    private fun setSignUpButtonDisenabled() {
        binding.signUpComplete.isEnabled = false
    }

    override fun onDestroyView() {
        mainScope.cancel()
        jobs.forEach { it.cancel() }
        super.onDestroyView()
    }

}
