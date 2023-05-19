package com.android.mediproject.feature.comments.commentsofamedicine

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.feature.comments.databinding.ItemViewCommentEditBinding
import com.android.mediproject.feature.comments.view.CommentItemView


class CommentsAdapter : ListAdapter<CommentDto, CommentsViewHolder>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder =
        CommentsViewHolder(CommentItemView(parent.context))

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CommentsViewHolder(private val view: CommentItemView) : RecyclerView.ViewHolder(view.rootView) {
    private var item: CommentDto? = null

    init {
        view.setOnReplyClickListener {
            item?.let { safeItem ->
                safeItem.onClickReply(safeItem.commentId)
            }
        }
        view.setOnLikeClickListener { commentId ->
            item?.let { safeItem ->
                safeItem.onClickLike(safeItem.commentId)
            }
        }
        view.setOnDeleteClickListener { commentId ->

        }

    }

    fun bind(commentDto: CommentDto) {
        item = commentDto
        view.setComment(commentDto)
    }
}

class Diff : DiffUtil.ItemCallback<CommentDto>() {
    override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem == newItem
    }
}

class CommentEditViewHolder(private val binding: ItemViewCommentEditBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.apply {
            commentEditButton.setOnClickListener {
                takeIf { !binding.commentInput.text.isNullOrBlank() && commentDto != null }?.apply {
                    commentDto?.apply {
                        onClickReplyEdited(copy(content = binding.commentInput.text.toString()))
                    }
                }
            }

            cancelButton.setOnClickListener {
                commentDto?.apply {
                    onClickEditCancel(absoluteAdapterPosition)
                }
            }
        }

        fun bind(commentDto: CommentDto) {
            binding.commentDto = commentDto
            binding.executePendingBindings()
        }

    }
}