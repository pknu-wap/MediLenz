package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentIntroBinding
import repeatOnStarted

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(FragmentIntroBinding::inflate) {

    override val fragmentViewModel: IntroViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = fragmentViewModel.apply {
            repeatOnStarted { eventFlow.collect { handelEvent(it) } }
        }
    }

    fun handelEvent(event: IntroViewModel.IntroEvent) = when (event) {
        is IntroViewModel.IntroEvent.NonMemberLogin -> {}
        is IntroViewModel.IntroEvent.MemberLogin -> {}
        is IntroViewModel.IntroEvent.SignUp -> {}
    }

}