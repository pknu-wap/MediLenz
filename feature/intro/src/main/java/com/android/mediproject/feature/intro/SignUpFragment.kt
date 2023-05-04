package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>(FragmentSignUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            log("회원가입 진입")
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }

    private fun handleEvent(event: SignUpViewModel.SignUpEvent) = when (event) {
        is SignUpViewModel.SignUpEvent.SignUpComplete -> {

        }
    }
}