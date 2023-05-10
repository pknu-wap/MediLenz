package com.android.mediproject.feature.comments.mycommentslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentMyCommnetsListBinding
import repeatOnStarted


class MyCommentsListFragment : BaseFragment<FragmentMyCommnetsListBinding, MyCommentsListViewModel>(
    FragmentMyCommnetsListBinding::inflate
) {
    override val fragmentViewModel: MyCommentsListViewModel by viewModels()
    private val myCommnetsListAdapter: MyCommentsListAdapter by lazy { MyCommentsListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }

            myCommentsListRV.apply {
                adapter = myCommnetsListAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }


        }

        myCommnetsListAdapter.submitList(
            mutableListOf(
                MyCommentDto(
                    12345,
                    "타이레놀",
                    "머리아플 때 먹으니까 짱 좋던데요..?",
                    "2023-03-30 22:48",
                    2
                ),
                MyCommentDto(
                    12346,
                    "코메키나",
                    "저 같은 비염환자들한테 딱 입니다. 시험칠 때 필수...!!",
                    "2023-03-30 22:48",
                    3
                ),
            )
        )
    }

    private fun handleEvent(event: MyCommentsListViewModel.MyCommentsListEvent) = when (event) {
        else -> {}
    }
}