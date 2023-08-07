package com.android.mediproject.feature.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.feature.mypage.databinding.ItemMypageMycommentBinding

class MyCommentListViewHolder(private val binding: ItemMypageMycommentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(myComment: MyCommentsListResponse.Comment) {
        binding.myCommentDto = myComment
    }

}

class MyPageMyCommentAdapter : ListAdapter<MyCommentsListResponse.Comment, MyCommentListViewHolder>(object :
    DiffUtil.ItemCallback<MyCommentsListResponse.Comment>() {
    override fun areItemsTheSame(oldItem: MyCommentsListResponse.Comment, newItem: MyCommentsListResponse.Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyCommentsListResponse.Comment, newItem: MyCommentsListResponse.Comment): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentListViewHolder {
        val binding =
            ItemMypageMycommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCommentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCommentListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
