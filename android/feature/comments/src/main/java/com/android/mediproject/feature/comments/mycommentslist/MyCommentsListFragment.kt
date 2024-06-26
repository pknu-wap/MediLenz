package com.android.mediproject.feature.comments.mycommentslist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentMyCommnetsListBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MyCommentsListFragment : BaseFragment<FragmentMyCommnetsListBinding, MyCommentsListViewModel>(
    FragmentMyCommnetsListBinding::inflate
) {
    override val fragmentViewModel: MyCommentsListViewModel by viewModels()
    private val myCommentsListAdapter: MyCommentsListAdapter by lazy { MyCommentsListAdapter() }

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(
            SystemBarStyler.StatusBarColor.BLACK,
            SystemBarStyler.NavigationBarColor.BLACK
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBarStyle()

        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }

            myCommentsListRV.apply {
                adapter = myCommentsListAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                addItemDecoration(MyCommentsListDecoration(requireContext()))
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
            }
        }

        //for Test
        myCommentsListAdapter.submitList(
            mutableListOf(
                MyCommentDto(
                    12345,
                    "타이레놀",
                    "머리아플 때 먹으니까 짱 좋던데요..?",
                    "2023-03-30 22:48",
                    2,
                    { comment ->
                        log(comment.medicineName + "을 누르셨습니다.")
                    }),
                MyCommentDto(
                    12346,
                    "코메키나",
                    "저 같은 비염환자들한테 딱 입니다. 시험칠 때 필수...!!",
                    "2023-03-30 22:48",
                    3,
                    { comment ->
                        log(comment.medicineName + "을 누르셨습니다.")
                    })
            )
        )
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    myCommentsListBar,
                    SystemBarStyler.SpacingType.PADDING
                )
            )
        )
    }

    private fun handleEvent(event: MyCommentsListViewModel.MyCommentsListEvent) = when (event) {
        else -> {}
    }
}