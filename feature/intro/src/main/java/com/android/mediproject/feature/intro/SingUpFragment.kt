package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentSingUpBinding

class SingUpFragment : BaseFragment<FragmentSingUpBinding, SignUpViewModel>(FragmentSingUpBinding::inflate) {

    override val fragmentViewModel: SignUpViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.apply {
                //repeatOnStarted { eventFlow.collect{ this@SingUpFragment.handelEvent(it) }}
            }
        }
    }

    /**
    fun handelEvent(event : SignUpViewModel.SignUpEvent) = when(event){
    else -> {}
    }
     **/
}