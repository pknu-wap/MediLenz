package com.android.mediproject.feature.intro

import MutableEventFlow
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import asEventFlow
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentSingUpBinding
import repeatOnStarted

class SingUpFragment : BaseFragment<FragmentSingUpBinding,SignUpViewModel>(FragmentSingUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.apply{
            viewModel.apply{
                repeatOnStarted { eventFlow.collect{handelEvent(it)}}
            }
        }
    }

    fun handelEvent(event : SignUpViewModel.SignUpEvent) = when(event){
        else -> {}
    }
}