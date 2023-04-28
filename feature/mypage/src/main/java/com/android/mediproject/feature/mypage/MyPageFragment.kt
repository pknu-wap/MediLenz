package com.android.mediproject.feature.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import repeatOnStarted

class MyPageFragment : BaseFragment<FragmentMyPageBinding,MyPageViewModel>(FragmentMyPageBinding::inflate) {
    override val fragmentViewModel: MyPageViewModel by viewModels()

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

    fun handelEvent(event : MyPageViewModel.MyPageEvent) = when(event){
        else -> {}
    }
}