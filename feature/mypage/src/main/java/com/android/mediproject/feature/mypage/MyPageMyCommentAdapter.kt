package com.android.mediproject.feature.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.feature.comments.databinding.ItemMyCommnetBinding

class MyCommentListViewHolder(private val binding: ItemMyCommnetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(myCommentDto: MyCommentDto) {
        binding.myComment = myCommentDto
    }
}

class MyPageMyCommentAdapter : ListAdapter<MyCommentDto, MyCommentListViewHolder>(object :
    DiffUtil.ItemCallback<MyCommentDto>() {
    override fun areItemsTheSame(oldItem: MyCommentDto, newItem: MyCommentDto): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: MyCommentDto, newItem: MyCommentDto): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentListViewHolder {
        val binding =
            ItemMyCommnetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCommentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCommentListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}