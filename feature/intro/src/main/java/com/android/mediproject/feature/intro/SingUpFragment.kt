package com.android.mediproject.feature.intro

import MutableEventFlow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.viewModels
import asEventFlow
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentSingUpBinding
import repeatOnStarted

@HiltViewModel
class SingUpFragment : BaseFragment<FragmentSingUpBinding,SignUpViewModel>(FragmentSingUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            log("회원가입 진입")
            viewModel = fragmentViewModel.apply{
                repeatOnStarted { eventFlow.collect{ handleEvent(it) }}
            }
        }
    }

    fun handleEvent(event : SignUpViewModel.SignUpEvent) = when(event){
        else -> {}
    }
}