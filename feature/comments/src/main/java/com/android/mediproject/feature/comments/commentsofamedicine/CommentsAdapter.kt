package com.android.mediproject.feature.comments.commentsofamedicine

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.feature.comments.view.CommentItemView


class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<CommentDto>() {
        override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem == newItem
        }
    })


    class CommentsViewHolder(private val view: CommentItemView) : RecyclerView.ViewHolder(view.rootView) {
        private var item: CommentDto? = null

        init {
            view.setOnReplyClickListener {
                item?.let { safeItem ->
                    safeItem.onClickReply(safeItem.commentId)
                }
            }
            view.setOnLikeClickListener() { commentId ->
                item?.let { safeItem ->
                    safeItem.onClickLike(safeItem.commentId)
                }
            }
            view.setOnDeleteClickListener() { commentId ->

            }
        }

        fun bind(commentDto: CommentDto) {
            item = commentDto
            view.setComment(commentDto)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder = CommentsViewHolder(
        CommentItemView(
            parent.context
        )
    )


    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size


}