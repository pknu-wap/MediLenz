package com.android.mediproject.feature.comments.commentsofamedicine

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.paging.setOnStateChangedListener
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.model.local.navargs.MedicineBasicInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.FragmentMedicineCommentsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class MedicineCommentsFragment :
    BaseFragment<FragmentMedicineCommentsBinding, MedicineCommentsViewModel>(FragmentMedicineCommentsBinding::inflate) {

    override val fragmentViewModel: MedicineCommentsViewModel by viewModels()

    private val medicineBasicInfoArgs by navArgs<MedicineBasicInfoArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel
            replyHeader.isVisible = false

            val adapter = CommentsAdapter().apply {
                setOnStateChangedListener(pagingListView.messageTextView,
                    pagingListView.pagingList,
                    pagingListView.progressIndicator,
                    getString(R.string.emptyComments))
            }

            pagingListView.pagingList.apply {
                /* RecyclerView 최적화
               뷰 홀더 캐시를 사용하지 않도록 설정
               캐시에 있는 뷰 홀더를 사용하면 onBindViewHolder()를 호출하지 않기 때문에 답글이나 수정 중인 댓글 아이템의 상태와
               뷰 홀더의 상태가 일치하지 않는 문제가 발생함
                */
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                    stackFromEnd = true
                    reverseLayout = true
                }
                setItemViewCacheSize(0)
                setRecycledViewPool(RecyclerView.RecycledViewPool().apply {
                    setMaxRecycledViews(CommentsAdapter.ViewType.COMMENT.ordinal, 6)
                    setMaxRecycledViews(CommentsAdapter.ViewType.EDITING.ordinal, 0)
                    setMaxRecycledViews(CommentsAdapter.ViewType.REPLY.ordinal, 6)
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
                                val text =
                                    HtmlCompat.fromHtml(getString(R.string.replyHeader) + action.comment, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                replayInfoHeader.text = text
                                replyHeader.isVisible = true
                            }

                            is CommentActionState.CLICKED_EDIT_COMMENT -> {
                                adapter.notifyItemChanged(action.position)
                            }

                            is CommentActionState.CLICKED_DELETE_MY_COMMENT -> {
                                showDialog(R.string.requestToDeleteComment, onPositive = {
                                    fragmentViewModel.deleteComment(action.commentId)
                                }, onNegative = {

                                })
                            }

                            is CommentActionState.COMPLETED_LIKE -> {
                                action.result.fold(onSuccess = {
                                    adapter.refresh()
                                }, onFailure = {
                                    toast(it.message.toString())
                                })
                            }

                            is CommentActionState.COMPLETED_APPLY_COMMENT_REPLY -> {
                                replayInfoHeader.isVisible = false
                                action.result.fold(onSuccess = {
                                    replyHeader.isVisible = false
                                    toast(getString(R.string.appliedComment))
                                    adapter.refresh()
                                }, onFailure = {
                                    toast(it.message.toString())
                                })
                            }

                            is CommentActionState.COMPLETED_APPLY_EDITED_COMMENT -> {
                                action.result.fold(onSuccess = {
                                    adapter.refresh()
                                    toast(getString(R.string.appliedEditComment))
                                }, onFailure = {
                                    toast(it.message.toString())
                                })
                            }

                            is CommentActionState.COMPLETED_DELETE_COMMENT -> {
                                action.result.fold(onSuccess = {
                                    adapter.refresh()
                                    toast(getString(R.string.deletedComment))
                                }, onFailure = {
                                    toast(it.message.toString())
                                })
                            }

                            is CommentActionState.NONE -> {
                            }

                            is CommentActionState.CANCELED_REPLY -> {
                                replyHeader.isVisible = false
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

            binding.commentInput.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    WindowCompat.getInsetsController(requireActivity().window, v).show(WindowInsetsCompat.Type.ime())
                }
            }

        }

        fragmentViewModel.setMedicineBasicInfo(medicineBasicInfoArgs)
    }


    private fun showDialog(@StringRes message: Int, onPositive: (Unit) -> Unit, onNegative: (Unit) -> Unit) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setMessage(getString(message))
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                onPositive(Unit)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                onNegative(Unit)
            }
        }.create().show()
    }
}