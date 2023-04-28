package com.android.mediproject.feature.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.splash.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(FragmentSplashBinding::inflate) {

    override val fragmentViewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = fragmentViewModel.apply {
            repeatOnStarted { eventFlow.collect { handelEvent(it) } }
        }
    }

    fun handelEvent(event: SplashViewModel.SplashEvent) = when (event) {
        is SplashViewModel.SplashEvent.TimerDone -> {
            log("스플래쉬끝")
        }
    }

}