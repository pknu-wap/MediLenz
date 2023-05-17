package com.android.mediproject.feature.comments.recentcommentlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.ui.base.view.SimpleListItemView

class RecentCommentsAdapter : ListAdapter<CommentDto, RecentCommentsAdapter.RecentCommentListViewHolder>(AsyncDiffer) {

    init {
        setHasStableIds(true)
    }

    class RecentCommentListViewHolder(private val view: SimpleListItemView<CommentDto>) : RecyclerView.ViewHolder(view) {

        private var item: CommentDto? = null

        init {
            view.setOnItemClickListener {
                item?.apply {
                    onClickDelete(commentId)
                    onClickLike(commentId)
                    onClickReply(commentId)
                }
            }
        }

        fun bind(data: CommentDto) {
            item = data
            view.setTitle(data.userName)
            view.setContent(data.content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCommentListViewHolder =
        RecentCommentListViewHolder(SimpleListItemView(parent.context))

    override fun onBindViewHolder(holder: RecentCommentListViewHolder, position: Int) {

    }


}


object AsyncDiffer : DiffUtil.ItemCallback<CommentDto>() {
    override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem == newItem
    }
}