package com.android.mediproject.feature.intro

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
import com.android.mediproject.core.common.util.delayTextChangedCallback
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentSignUpBinding
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
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>(FragmentSignUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()

    @Inject @Dispatcher(MediDispatchers.Default) lateinit var defaultDispatcher: CoroutineDispatcher

    private val mainScope = MainScope()

    private val jobs = mutableListOf<Job>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }

            addDelayTextWatcher(signUpEmail.inputData)
            addDelayTextWatcher(signUpPassword.inputData)
            addDelayTextWatcher(signUpPasswordCheck.inputData)
            addDelayTextWatcher(signUpNickName.inputData)

            signUpComplete.setOnClickListener {
                fragmentViewModel.signUp(
                    binding.signUpEmail.getEditable(),
                    binding.signUpPassword.getEditable(),
                    binding.signUpPasswordCheck.getEditable(),
                    binding.signUpNickName.getEditable()
                )
            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.signInEvent.collectLatest {
                    when (it) {
                        is SignUpState.SigningUp -> {
                            LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.signingUp))
                        }

                        is SignUpState.SuccessSignUp -> {
                            LoadingDialog.dismiss()
                            toast(getString(R.string.signUpSuccess))
                            findNavController().navigate(
                                "medilens://main/home_nav".toUri(),
                                NavOptions.Builder().setPopUpTo(R.id.signUpFragment, true).build()
                            )
                        }

                        is SignUpState.FailedSignUp -> {
                            // 실패
                            LoadingDialog.dismiss()
                            toast(getString(R.string.signUpFailed))
                        }

                        is SignUpState.RegexError -> {
                            // 이메일 또는 비밀번호 형식 오류
                            toast(getString(R.string.signInRegexError))
                        }

                        is SignUpState.Initial -> {
                        }

                        is SignUpState.PasswordError -> {
                            toast(getString(R.string.signUpPasswordError))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        mainScope.cancel()
        jobs.forEach { it.cancel() }
        super.onDestroyView()
    }

    private fun handleEvent(event: SignUpViewModel.SignUpEvent) = when (event) {
        is SignUpViewModel.SignUpEvent.SignUpComplete -> {
            binding.apply {
                log(
                    signUpEmail.getValue() + "\n" + signUpPassword.getValue() + "\n" + signUpPasswordCheck.getValue() + "\n" + signUpNickName.getValue()
                )
            }
            //Todo
        }
    }

    private fun addDelayTextWatcher(editText: EditText) {
        mainScope.launch(context = defaultDispatcher + Job().apply {
            jobs.add(this)
        }) {
            editText.delayTextChangedCallback().debounce(500L).onEach { seq ->
                mainScope.launch {
                    binding.signUpComplete.isEnabled = !seq.isNullOrEmpty()
                }
            }.launchIn(this)
        }
    }
}