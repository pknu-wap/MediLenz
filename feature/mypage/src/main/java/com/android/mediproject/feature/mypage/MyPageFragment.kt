package com.android.mediproject.feature.mypage

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import repeatOnStarted

class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel>(FragmentMyPageBinding::inflate) {
    override val fragmentViewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.MyCommentsList -> findNavController().navigate("medilens://main/comments_nav/myCommentsListFragment".toUri())
        is MyPageViewModel.MyPageEvent.InterestedMedicineList -> findNavController().navigate("medilens://main/moreInterestedMedicine_nav".toUri())
    }
}