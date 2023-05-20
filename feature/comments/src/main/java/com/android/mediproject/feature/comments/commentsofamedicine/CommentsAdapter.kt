package com.android.mediproject.feature.comments.commentsofamedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.ItemViewCommentEditBinding
import com.android.mediproject.feature.comments.view.CommentItemView


class CommentsAdapter : PagingDataAdapter<CommentDto, BaseCommentViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewType.let {
        when (it) {
            CommentType.COMMENT.ordinal -> CommentsViewHolder(CommentItemView(parent.context))
            CommentType.EDITING.ordinal -> CommentEditViewHolder(
                ItemViewCommentEditBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseCommentViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    override fun onViewRecycled(holder: BaseCommentViewHolder) {
        super.onViewRecycled(holder)
        // holder.fixBind()
    }

    /**
     * 현재 댓글 수정 상태이면 1, 아니면 0 반환
     */
    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if (it.isEditing) CommentType.EDITING.ordinal else CommentType.COMMENT.ordinal
        } ?: CommentType.COMMENT.ordinal
    }
}


/**
 * 댓글 아이템 뷰 홀더
 *
 * type : 0
 */
class CommentsViewHolder(private val view: CommentItemView) : BaseCommentViewHolder(view.rootView) {
    private var item: CommentDto? = null

    init {
        view.setOnReplyClickListener {
            item?.also { safeItem ->
                safeItem.onClickReply?.invoke(safeItem.commentId)
            }
        }
        view.setOnLikeClickListener {
            item?.also { safeItem ->
                safeItem.onClickLike?.invoke(safeItem.commentId)
            }
        }
        view.setOnMoreClickListener {
            item?.takeIf { it.isMine }?.also { safeItem ->
                MediPopupMenu.showMenu(it, R.menu.comment_action_menu) { menuItem ->
                    when (menuItem.itemId) {
                        R.id.deleteMyComment -> safeItem.onClickDelete?.invoke(absoluteAdapterPosition)
                        R.id.editMyComment -> {
                            safeItem.isEditing = true
                            safeItem.onClickEdit?.invoke(absoluteAdapterPosition)
                        }
                    }
                    true
                }
            }
        }

    }

    override fun bind(commentDto: CommentDto) {
        item = commentDto
        view.setComment(commentDto)
    }

    override fun fixBind() {
        item?.also {
            view.setComment(it)
        }
    }
}


/**
 * 댓글 수정 뷰홀더
 *
 * type : 1
 */
class CommentEditViewHolder(private val binding: ItemViewCommentEditBinding) : BaseCommentViewHolder(binding.root) {
    init {
        binding.apply {
            commentEditButton.setOnClickListener {
                takeIf { !binding.commentInput.text.isNullOrBlank() && commentDto != null }?.apply {
                    commentDto?.apply {
                        onClickApplyEdited?.invoke(copy(content = binding.commentInput.text.toString()), absoluteAdapterPosition)
                    }
                }
            }

            cancelButton.setOnClickListener {
                commentDto?.apply {
                    isEditing = false
                    onClickEditCancel?.invoke(absoluteAdapterPosition)
                }
            }
        }

    }

    override fun bind(commentDto: CommentDto) {
        binding.commentDto = commentDto
        binding.executePendingBindings()
    }

    override fun fixBind() {

    }
}

abstract class BaseCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(commentDto: CommentDto) {}

    open fun fixBind() {}
}

object Diff : DiffUtil.ItemCallback<CommentDto>() {
    override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean = oldItem.commentId == newItem.commentId


    override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean = oldItem == newItem

}

enum class CommentType {
    COMMENT, EDITING
}