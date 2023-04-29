package com.android.mediproject.feature.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.community.databinding.FragmentCommunityBinding
import repeatOnStarted

class CommunityFragment :
    BaseFragment<FragmentCommunityBinding, CommunityViewModel>(FragmentCommunityBinding::inflate) {
    override val fragmentViewModel: CommunityViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect {  handleEvent(it) } }
            }
        }
    }

    fun handleEvent(event: CommunityViewModel.CommunityEvent) = when (event) {
        else -> {}
    }
}