package com.android.mediproject.feature.intro


import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.delayTextChangedCallback
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import repeatOnStarted
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LoginFragment @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineContext
) : BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {
    override val fragmentViewModel: LoginViewModel by viewModels()

    private val mainScope = MainScope()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginBtn.setOnClickListener {
                fragmentViewModel.signIn(binding.loginEmail.getEditable(), binding.loginPassword.getEditable())
            }

            loginBtn.isEnabled = false

            addDelayTextWatcher(loginEmail.inputData)
            addDelayTextWatcher(loginPassword.inputData)

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.signInEvent.collectLatest {
                    when (it) {
                        is SignInState.Signing -> {
                            // 로그인 중
                            LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.signing))
                        }

                        is SignInState.SuccessSignIn -> {
                            // 로그인 성공
                            LoadingDialog.dismiss()
                        }

                        is SignInState.FailedSignIn -> {
                            // 로그인 실패
                            LoadingDialog.dismiss()
                        }

                        is SignInState.RegexError -> {
                            // 이메일 또는 비밀번호 형식 오류
                            toast(getString(R.string.signInRegexError))
                        }

                        is SignInState.SignOut -> {
                            // 로그아웃
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    private fun handleEvent(event: LoginViewModel.SignEvent) = when (event) {
        is LoginViewModel.SignEvent.SignIn -> {
        }

        is LoginViewModel.SignEvent.SignUp -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    private fun addDelayTextWatcher(editText: EditText) {
        mainScope.launch(context = defaultDispatcher + Job()) {
            editText.delayTextChangedCallback().debounce(500L).onEach { seq ->
                binding.loginBtn.isEnabled = !seq.isNullOrEmpty()
            }.launchIn(this)
        }
    }
}