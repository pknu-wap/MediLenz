package com.android.mediproject.feature.comments.mycommentslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.feature.comments.databinding.ItemMyCommentBinding

class MyCommentsViewHolder(
    private val binding: ItemMyCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: CommentListResponse.Comment) {
        binding.comment = comment
    }
}

class MyCommentsListAdapter : ListAdapter<CommentListResponse.Comment, MyCommentsViewHolder>(
    object : DiffUtil.ItemCallback<CommentListResponse.Comment>() {
        override fun areItemsTheSame(oldItem: CommentListResponse.Comment, newItem: CommentListResponse.Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CommentListResponse.Comment, newItem: CommentListResponse.Comment): Boolean {
            return oldItem == newItem
        }
    },
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentsViewHolder {
        val binding =
            ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
