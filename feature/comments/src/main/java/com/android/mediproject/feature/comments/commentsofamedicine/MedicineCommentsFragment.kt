package com.android.mediproject.feature.comments.commentsofamedicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.paging.setOnStateChangedListener
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.CommentActionType
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.FragmentMedicineCommentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class MedicineCommentsFragment :
    BaseFragment<FragmentMedicineCommentsBinding, MedicineCommentsViewModel>(FragmentMedicineCommentsBinding::inflate) {

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
                this.adapter = adapter
            }

            viewLifecycleOwner.repeatOnStarted {

                launch {
                    fragmentViewModel.action.collect { action ->
                        when (action.commentActionType) {
                            CommentActionType.EDIT -> {
                                // 댓글 수정
                                adapter.notifyItemChanged(action.position)
                            }

                            CommentActionType.DELETE -> {
                                // 댓글 삭제
                                adapter.notifyItemRemoved(action.position)
                            }

                            CommentActionType.REPLY -> {
                                // 댓글 답글
                            }

                            CommentActionType.LIKE -> {
                                // 댓글 좋아요
                            }

                            CommentActionType.CANCEL_EDIT -> {
                                // 댓글 수정 취소
                                adapter.notifyItemChanged(action.position)
                            }

                            CommentActionType.APPLYED_EDITED_COMMENT -> {
                                // 댓글 수정 적용
                                toast(getString(R.string.successAddComment))
                            }

                            CommentActionType.APPLYED_NEW_COMMENT -> {
                                // 댓글 추가
                                toast(getString(R.string.successAddComment))
                            }
                        }
                    }
                }


                launch {
                    fragmentViewModel.comments.collect {
                        adapter.submitData(it)
                    }
                }
            }

            sendBtn.setOnClickListener {
                commentInput.takeIf { !it.text.isNullOrBlank() }?.also { safeCommentInput ->
                    fragmentViewModel.applyNewComment(safeCommentInput.text.toString())
                    safeCommentInput.setText("")
                } ?: toast(getString(R.string.commentInputHint))
            }
        }

    }
}