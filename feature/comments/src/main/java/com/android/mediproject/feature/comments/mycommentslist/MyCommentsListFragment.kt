package com.android.mediproject.feature.comments.mycommentslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentMyCommnetsListBinding


class MyCommentsListFragment : BaseFragment<FragmentMyCommnetsListBinding,MyCommentsListViewModel>(FragmentMyCommnetsListBinding::inflate) {
    override val fragmentViewModel: MyCommentsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = fragmentViewModel.apply{

            }


        }
    }
}