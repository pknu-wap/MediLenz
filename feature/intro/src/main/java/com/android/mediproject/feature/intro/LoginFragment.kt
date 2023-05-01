package com.android.mediproject.feature.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel>(FragmentLoginBinding::inflate) {
    override val fragmentViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("로그인 진입")
        binding.apply{
            viewModel = fragmentViewModel.apply{
                repeatOnStarted { eventFlow.collect{handleEvent(it)} }
            }
        }
    }

    fun handleEvent(event : LoginViewModel.LoginEvent) = when(event){
        else -> {}
    }
}