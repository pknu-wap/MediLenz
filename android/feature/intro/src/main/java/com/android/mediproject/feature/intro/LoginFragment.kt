package com.android.mediproject.feature.intro


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.delayTextChangedCallback
import com.android.mediproject.core.model.local.navargs.TOHOME
import com.android.mediproject.core.model.local.navargs.TOMYPAGE
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentLoginBinding
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
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {
    override val fragmentViewModel: LoginViewModel by viewModels()

    @Inject
    @Dispatcher(MediDispatchers.Default)
    lateinit var defaultDispatcher: CoroutineDispatcher

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    private val mainScope = MainScope()
    private val jobs = mutableListOf<Job>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(
            SystemBarStyler.StatusBarColor.BLACK,
            SystemBarStyler.NavigationBarColor.BLACK
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarStyle()

        binding.apply {


            loginBtn.isEnabled = false
            val moveFlag = arguments?.getInt("flag", TOHOME)
            fragmentViewModel.setMoveFlag(moveFlag ?: TOHOME)

            addDelayTextWatcher(loginEmail.inputData)
            addDelayTextWatcher(loginPassword.inputData)

            viewLifecycleOwner.repeatOnStarted {
                viewModel = fragmentViewModel

                launch {
                    fragmentViewModel.eventFlow.collect { handleEvent(it) }
                }

                launch {
                    fragmentViewModel.signInEvent.collectLatest {
                        when (it) {
                            is SignInState.Signing -> {
                                // 로그인 중
                                LoadingDialog.showLoadingDialog(
                                    requireActivity(),
                                    getString(R.string.signing)
                                )
                            }

                            is SignInState.SuccessSignIn -> {
                                // 로그인 성공
                                LoadingDialog.dismiss()
                                toast(getString(R.string.signInSuccess))

                                when (fragmentViewModel.moveFlag.value) {

                                    TOHOME -> findNavController().navigate(
                                        "medilens://main/home_nav".toUri(),
                                        NavOptions.Builder().setPopUpTo(R.id.loginFragment, true)
                                            .build()
                                    )

                                    TOMYPAGE -> findNavController().navigate(
                                        "medilens://main/mypage_nav".toUri(),
                                        NavOptions.Builder().setPopUpTo(R.id.loginFragment, true)
                                            .build()
                                    )

                                }
                            }

                            is SignInState.FailedSignIn -> {
                                // 로그인 실패
                                LoadingDialog.dismiss()
                                toast(getString(R.string.signInFailed))
                            }

                            is SignInState.RegexError -> {
                                // 이메일 또는 비밀번호 형식 오류
                                toast(getString(R.string.signInRegexError))
                            }

                            is SignInState.Initial -> {
                                // 로그아웃 상태
                            }
                        }
                    }
                }

                launch {
                    fragmentViewModel.savedEmail.collectLatest {
                        if (it.isNotEmpty()) {
                            binding.rememberEmailCB.isChecked = true
                            binding.loginEmail.inputData.setText(it, 0, it.size)
                        }
                    }
                }

            }
        }
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    loginBar,
                    SystemBarStyler.SpacingType.PADDING
                )
            )
        )
    }

    override fun onDestroyView() {
        mainScope.cancel()
        jobs.forEach { it.cancel() }
        super.onDestroyView()
    }

    private fun handleEvent(event: LoginViewModel.SignEvent) = when (event) {
        is LoginViewModel.SignEvent.SignIn -> {


            fragmentViewModel.signIn(
                binding.loginEmail.getEditable(),
                binding.loginPassword.getEditable(),
                binding.rememberEmailCB.isChecked
            )

        }

        is LoginViewModel.SignEvent.SignUp -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    private fun addDelayTextWatcher(editText: EditText) {
        mainScope.launch(context = defaultDispatcher + Job().apply {
            jobs.add(this)
        }) {
            editText.delayTextChangedCallback().debounce(500L).onEach { seq ->
                mainScope.launch {
                    binding.loginBtn.isEnabled = !seq.isNullOrEmpty()
                }
            }.launchIn(this)
        }
    }
}