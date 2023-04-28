package com.android.mediproject.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDeepLinkRequest.Builder.Companion.fromUri
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.splash.databinding.FragmentSplashBinding
import repeatOnStarted

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(FragmentSplashBinding::inflate) {

    override val fragmentViewModel: SplashViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = fragmentViewModel.apply{
            repeatOnStarted { eventFlow.collect{handelEvent(it)} }
        }
    }

    fun handelEvent(event : SplashViewModel.SplashEvent) = when(event){
        is SplashViewModel.SplashEvent.TimerDone -> {
            val request = NavDeepLinkRequest.Builder
                .fromUri("medilens://main/home_nav".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

}