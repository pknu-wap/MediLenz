package com.android.mediproject.feature.comments.recentcommentlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.Comment
import com.android.mediproject.core.ui.base.view.SimpleListItemView

class RecentCommentsAdapter : ListAdapter<Comment, RecentCommentsAdapter.RecentCommentListViewHolder>(AsyncDiffer) {


    class RecentCommentListViewHolder(private val view: SimpleListItemView<Comment>) : RecyclerView.ViewHolder(view) {

        private var item: Comment? = null

        init {
            view.setOnItemClickListener {
                item?.apply {
                    onClickDelete?.invoke(commentId)
                    onClickLike?.invoke(commentId, !isLiked)
                    onClickReply?.invoke(content, commentId)
                }
            }
        }

        fun bind(data: Comment) {
            item = data
            view.setTitle(data.userName)
            view.setContent(data.content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCommentListViewHolder =
        RecentCommentListViewHolder(SimpleListItemView(parent.context, 0.6f))

    override fun onBindViewHolder(holder: RecentCommentListViewHolder, position: Int) {

    }


}


object AsyncDiffer : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}
