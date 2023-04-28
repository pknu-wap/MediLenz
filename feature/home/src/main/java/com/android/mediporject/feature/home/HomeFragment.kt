package com.android.mediporject.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import repeatOnStarted

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val fragmentViewModel: HomeViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.apply{
            viewModel = fragmentViewModel.apply{
                repeatOnStarted { eventFlow.collect{ handelEvent(it)} }
            }
        }
    }

    fun handelEvent(event : HomeViewModel.HomeEvent) = when(event){
        else -> {}
    }

}