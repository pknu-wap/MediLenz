package com.android.mediproject.feature.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.community.databinding.FragmentCommunityBinding
import repeatOnStarted

class CommunityFragment :
    BaseFragment<FragmentCommunityBinding, CommunityViewModel>(FragmentCommunityBinding::inflate) {
    override val fragmentViewModel: CommunityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }

    private fun handleEvent(event: CommunityViewModel.CommunityEvent) = when (event) {
        else -> {}
    }
}