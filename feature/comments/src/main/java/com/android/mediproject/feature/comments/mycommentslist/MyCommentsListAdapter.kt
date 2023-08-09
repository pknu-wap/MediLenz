package com.android.mediproject.feature.comments.mycommentslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.feature.comments.databinding.ItemMyCommentBinding

class MyCommentsViewHolder(
    private val binding: ItemMyCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: MyCommentsListResponse.Comment) {
        binding.comment = comment
    }
}

class MyCommentsListAdapter : ListAdapter<MyCommentsListResponse.Comment, MyCommentsViewHolder>(
    object : DiffUtil.ItemCallback<MyCommentsListResponse.Comment>() {
        override fun areItemsTheSame(oldItem: MyCommentsListResponse.Comment, newItem: MyCommentsListResponse.Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MyCommentsListResponse.Comment, newItem: MyCommentsListResponse.Comment): Boolean {
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
