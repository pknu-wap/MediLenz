package com.android.mediproject.feature.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel>(FragmentLoginBinding::inflate) {
    override val fragmentViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            viewModel = fragmentViewModel.apply{
                repeatOnStarted { eventFlow.collect{handleEvent(it)} }
            }
        }
    }

    fun handleEvent(event : LoginViewModel.LoginEvent) = when(event){
        is LoginViewModel.LoginEvent.Login -> {
            val id = binding.loginEmail.getValue()
            val password = binding.loginPassword.getValue()
            log("아이디 : "+id +" 비밀번호 : "+password) }

        is LoginViewModel.LoginEvent.SignUp -> {
            log("회원가입")
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment()) }
    }
}