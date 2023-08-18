package com.android.mediproject.feature.comments.commentsofamedicine

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.paging.setOnStateChangedListener
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.navargs.MedicineBasicInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.FragmentMedicineCommentsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MedicineCommentsFragment : BaseFragment<FragmentMedicineCommentsBinding, MedicineCommentsViewModel>(FragmentMedicineCommentsBinding::inflate) {

    override val fragmentViewModel: MedicineCommentsViewModel by viewModels()

    private val medicineBasicInfoArgs by navArgs<MedicineBasicInfoArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel

            commentInputView.setOnClickListener {
                CommentDialog.show(requireActivity(), fragmentViewModel)
            }

            val adapter = CommentsAdapter().apply {
                setOnStateChangedListener(
                    pagingListView.messageTextView,
                    pagingListView.pagingList,
                    pagingListView.progressIndicator,
                    getString(R.string.emptyComments),
                )
            }

            pagingListView.pagingList.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                    stackFromEnd = true
                    reverseLayout = true
                }
                setItemViewCacheSize(0)
                setRecycledViewPool(
                    RecyclerView.RecycledViewPool().apply {
                        setMaxRecycledViews(CommentsAdapter.ViewType.COMMENT.ordinal, 6)
                        setMaxRecycledViews(CommentsAdapter.ViewType.EDITING.ordinal, 1)
                        setMaxRecycledViews(CommentsAdapter.ViewType.REPLY.ordinal, 6)
                    },
                )
                this.adapter = adapter
            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.action.collect { action ->
                    action.onNone {

                    }.onOnCancelReply {
                    }.onOnClickEditComment { positionOnList ->
                        adapter.notifyItemChanged(positionOnList)
                    }.onOnClickToDeleteComment { commentId ->
                        showDialog(
                            R.string.requestToDeleteComment,
                            onPositive = {
                                fragmentViewModel.deleteComment(commentId)
                            },
                            onNegative = {

                            },
                        )
                    }.onOnClickToLike { }.onOnCompleteLike { result ->
                        if (result) {
                            toast(getString(R.string.completedLike))
                            adapter.refresh()
                        }
                    }.onOnCompleteApplyCommentOrReply { result ->
                        if (result) {
                            toast(getString(R.string.appliedComment))
                            adapter.refresh()
                        }
                    }.onOnCompleteApplyEditComment { result ->
                        if (result) {
                            toast(getString(R.string.appliedEditComment))
                            adapter.refresh()
                        }
                    }.onOnCompleteDeleteComment { result ->
                        if (result) {
                            toast(getString(R.string.deletedComment))
                            adapter.refresh()
                        }
                    }.onOnClickToReply { comment ->

                    }
                }

            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.comments.collectLatest {
                    adapter.submitData(it)
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
