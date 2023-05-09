package com.android.mediproject.feature.comments.mycommnetsllist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.FragmentMyCommnetsListBinding


class MyCommnetsListFragment : BaseFragment<FragmentMyCommnetsListBinding,MyCommentsListViewModel>(FragmentMyCommnetsListBinding::inflate) {
    override val fragmentViewModel: MyCommentsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = fragmentViewModel.apply{

            }
        }
    }
}