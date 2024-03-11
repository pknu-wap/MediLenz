package com.android.mediproject.feature.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.delayTextChangedCallback
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.navargs.TOHOME
import com.android.mediproject.core.model.navargs.TOMYPAGE
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {
    override val fragmentViewModel: LoginViewModel by viewModels()

    @Inject @Dispatcher(MediDispatchers.Default) lateinit var defaultDispatcher: CoroutineDispatcher

    @Inject lateinit var systemBarStyler: SystemBarController

    private val mainScope = MainScope() + SupervisorJob()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                repeatOnStarted {
                    loginState.collectLatest { handleSignInState(it) }
                }
                repeatOnStarted {
                    savedEmail.collectLatest { callSavedEmail(it) }
                }
            }
        }
        setBarStyle()
        setCallBackMoveFlag()
        setDelayTextWatcher()
        setLoginButtonDisabled()
    }

    private fun handleEvent(event: LoginViewModel.LoginEvent) = when (event) {
        is LoginViewModel.LoginEvent.Login -> login()
        is LoginViewModel.LoginEvent.SignUp -> signUp()
    }

    private fun login() {
        fragmentViewModel.loginWithCheckRegex(
            binding.loginEmail.getValue(),
            binding.loginPassword.getValue(),
            binding.rememberEmailCB.isChecked,
        )
    }

    private fun signUp() {
        navigateWithNavDirections(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    private fun handleSignInState(loginState: LoginViewModel.LoginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Logining -> showLoadingDialog()
            is LoginViewModel.LoginState.LoginSuccess -> loginSuccess()
            is LoginViewModel.LoginState.LoginFailed -> loginFailed()
            is LoginViewModel.LoginState.RegexError -> regexError()
            is LoginViewModel.LoginState.Initial -> initial()
        }
    }

    private fun showLoadingDialog() {
        LoadingDialog.showLoadingDialog(
            requireActivity(),
            getString(R.string.signing),
        )
    }

    private fun loginSuccess() {
        LoadingDialog.dismiss()
        toast(getString(R.string.signInSuccess))
        handleCallBackMoveFlag()
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

    private fun loginFailed() {
        LoadingDialog.dismiss()
        toast(getString(R.string.signInFailed))
    }

    private fun regexError() {
        toast(getString(R.string.signInRegexError))
    }

    private fun initial() {
    }

    private fun callSavedEmail(savedEmail: CharArray) {
        if (savedEmail.isNotEmpty()) {
            binding.apply {
                rememberEmailCB.isChecked = true
                loginEmail.inputData.setText(savedEmail, 0, savedEmail.size)
            }
        }
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    loginBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }

    private fun setCallBackMoveFlag() {
        val moveFlag = arguments?.getInt("flag", TOHOME)
        fragmentViewModel.setMoveFlag(moveFlag ?: TOHOME)
    }

    private fun setDelayTextWatcher() = binding.apply {
        loginEmail.inputData.delayTextChangedCallback().debounce(500L).combine(
            loginPassword.inputData.delayTextChangedCallback().debounce(500L),
        ) { e, p ->
            e to p
        }.flowOn(defaultDispatcher).onEach {
            binding.loginBtn.isEnabled = !it.first.isNullOrEmpty() && !it.second.isNullOrEmpty()
        }.launchIn(mainScope)
    }

    private fun setLoginButtonDisabled() {
        binding.loginBtn.isEnabled = false
    }

    override fun onDestroyView() {
        mainScope.cancel()
        super.onDestroyView()
    }

}
