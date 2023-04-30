package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentIntroBinding
import repeatOnStarted

class IntroFragment :
    BaseFragment<FragmentIntroBinding, IntroViewModel>(FragmentIntroBinding::inflate) {

    override val fragmentViewModel: IntroViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = fragmentViewModel.apply {
            repeatOnStarted { eventFlow.collect { handleEvent(it) } }
        }
    }

    fun handleEvent(event: IntroViewModel.IntroEvent) = when (event) {
        is IntroViewModel.IntroEvent.NonMemberLogin -> {
            val request = NavDeepLinkRequest.Builder
                .fromUri("medilens://main/home_nav".toUri())
                .build()
            findNavController().navigate(request)
        }
        is IntroViewModel.IntroEvent.MemberLogin -> {}
        is IntroViewModel.IntroEvent.SignUp -> {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToSingUpFragment())
        }
    }

}