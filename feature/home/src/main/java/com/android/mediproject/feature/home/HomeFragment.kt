package com.android.mediproject.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment() : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    override val fragmentViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}