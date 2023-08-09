package com.android.mediproject.feature.comments.mycommentslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentMyCommentsListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCommentsListFragment : BaseFragment<FragmentMyCommentsListBinding, MyCommentsListViewModel>(
    FragmentMyCommentsListBinding::inflate,
) {
    override val fragmentViewModel: MyCommentsListViewModel by viewModels()
    private val myCommentsListAdapter: MyCommentsListAdapter by lazy { MyCommentsListAdapter() }

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            repeatOnStarted { myCommentsList.collect { handleMyCommentListState(it) } }
            loadMyCommentsList()
        }
        setBarStyle()
        setRecyclerView()
    }

    private fun handleEvent(event: MyCommentsListViewModel.MyCommentsListEvent) = when (event) {
        else -> {}
    }

    private fun handleMyCommentListState(commentListState: UiState<List<MyCommentsListResponse.Comment>>) {
        when (commentListState) {
            is UiState.Init -> {}

            is UiState.Loading -> setCommentLoadingVisible()

            is UiState.Success -> {
                setCommentSuccessVisible()
                myCommentsListAdapter.submitList(commentListState.data)
            }

            is UiState.Error -> log(commentListState.message)
        }
    }

    private fun setCommentLoadingVisible() = binding.apply{
        commentLottie.visibility = View.VISIBLE
        myCommentsListRV.visibility = View.GONE
    }

    private fun setCommentSuccessVisible() = binding.apply{
        commentLottie.visibility = View.GONE
        myCommentsListRV.visibility = View.VISIBLE
    }

    private fun setRecyclerView() = binding.myCommentsListRV.apply {
        adapter = myCommentsListAdapter
        layoutManager = LinearLayoutManager(requireActivity())
        addItemDecoration(MyCommentsListDecoration(requireContext()))
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    myCommentsListBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }
}
