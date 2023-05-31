package com.android.mediproject.feature.comments.commentsofamedicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.paging.setOnStateChangedListener
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.FragmentMedicineCommentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class MedicineCommentsFragment :
    BaseFragment<FragmentMedicineCommentsBinding, MedicineCommentsViewModel>(
        FragmentMedicineCommentsBinding::inflate
    ) {

    override val fragmentViewModel: MedicineCommentsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel

            val adapter = CommentsAdapter().apply {
                setOnStateChangedListener(
                    pagingListView.messageTextView,
                    pagingListView.pagingList,
                    pagingListView.progressIndicator,
                    getString(R.string.emptyComments)
                )
            }

            pagingListView.pagingList.apply {
                /* RecyclerView 최적화
               뷰 홀더 캐시를 사용하지 않도록 설정
               캐시에 있는 뷰 홀더를 사용하면 onBindViewHolder()를 호출하지 않기 때문에 답글이나 수정 중인 댓글 아이템의 상태와
               뷰 홀더의 상태가 일치하지 않는 문제가 발생함
                */
                setItemViewCacheSize(0)
                setRecycledViewPool(RecyclerView.RecycledViewPool().apply {
                    setMaxRecycledViews(CommentsAdapter.CommentType.COMMENT.ordinal, 6)
                    setMaxRecycledViews(CommentsAdapter.CommentType.EDITING.ordinal, 1)
                })
                this.adapter = adapter
            }

            viewLifecycleOwner.repeatOnStarted {

                launch {
                    fragmentViewModel.action.collect { action ->
                        when (action) {
                            is CommentActionState.CLICKED_LIKE -> {
                            }

                            is CommentActionState.CLICKED_REPLY -> {
                                adapter.notifyItemChanged(action.position)
                            }

                            is CommentActionState.CLICKED_EDIT_COMMENT -> {
                                adapter.notifyItemChanged(action.position)
                            }

                            is CommentActionState.CLICKED_DELETE_MY_COMMENT -> {

                            }

                            is CommentActionState.COMPLETED_LIKE -> {
                                adapter.refresh()
                            }

                            is CommentActionState.COMPLETED_APPLY_COMMENT_REPLY -> {
                                adapter.refresh()
                            }

                            is CommentActionState.COMPLETED_APPLY_EDITED_COMMENT -> {
                                adapter.refresh()
                            }

                            is CommentActionState.COMPLETED_DELETE_COMMENT -> {
                                adapter.refresh()
                            }

                            is CommentActionState.ERROR -> {
                                toast(action.errorMessage)
                            }

                            is CommentActionState.NONE -> {
                            }
                        }
                    }
                }

                launch {
                    fragmentViewModel.comments.collectLatest {
                        adapter.submitData(it)
                    }
                }
            }

        }

    }
}