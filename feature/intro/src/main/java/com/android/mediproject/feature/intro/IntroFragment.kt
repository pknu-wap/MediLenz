package com.android.mediproject.feature.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.intro.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import com.android.mediproject.core.common.viewmodel.repeatOnStarted

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(FragmentIntroBinding::inflate) {

    override val fragmentViewModel: IntroViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewLifecycleOwner.repeatOnStarted { fragmentViewModel.eventFlow.collect { handleEvent(it) } }
    }

    private fun handleEvent(event: IntroViewModel.IntroEvent) {
        when (event) {
            is IntroViewModel.IntroEvent.GuestLogin, IntroViewModel.IntroEvent.SkipIntro -> skipIntro()
            is IntroViewModel.IntroEvent.ShowIntro -> nonSkipIntro()
            is IntroViewModel.IntroEvent.MemberLogin -> memberLogin()
            is IntroViewModel.IntroEvent.SignUp -> signUp()
        }
    }

    private fun skipIntro() {
        navigateWithUriNavOptions("medilens://main/home_nav", NavOptions.Builder().setPopUpTo(R.id.introFragment, true).build())
    }

    private fun nonSkipIntro() = binding.viewStub.apply {
        viewStub?.inflate()
        binding?.apply {
            setVariable(BR.viewModel, fragmentViewModel)
            executePendingBindings()
        }
    }

    private fun memberLogin() {
        navigateWithNavDirections(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
    }

    private fun signUp() {
        navigateWithNavDirections(IntroFragmentDirections.actionIntroFragmentToSignUpFragment())
    }

}
