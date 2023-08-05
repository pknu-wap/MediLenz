package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(FragmentIntroBinding::inflate) {

    override val fragmentViewModel: IntroViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.repeatOnStarted { fragmentViewModel.eventFlow.collect { handleEvent(it) } }
    }

    private fun handleEvent(event: IntroViewModel.IntroEvent) = when (event) {
        is IntroViewModel.IntroEvent.NonMemberLogin, IntroViewModel.IntroEvent.SkipIntro -> {
            findNavController().navigate(
                "medilens://main/home_nav".toUri(), NavOptions.Builder().setPopUpTo(R.id.introFragment, true).build()
            )
        }

        is IntroViewModel.IntroEvent.NonSkipIntro -> {
            binding.viewStub.viewStub?.inflate()
            binding.viewStub.binding?.setVariable(BR.viewModel, fragmentViewModel)
            binding.viewStub.binding?.executePendingBindings()
        }

        is IntroViewModel.IntroEvent.MemberLogin -> findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
        is IntroViewModel.IntroEvent.SignUp -> findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToSignUpFragment())
    }

}