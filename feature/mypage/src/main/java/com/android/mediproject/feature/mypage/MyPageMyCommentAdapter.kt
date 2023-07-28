package com.android.mediproject.feature.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.MyComment
import com.android.mediproject.feature.mypage.databinding.ItemMypageMycommentBinding

class MyCommentListViewHolder(private val binding: ItemMypageMycommentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(myComment: MyComment) {
        binding.myCommentDto = myComment
    }

}

class MyPageMyCommentAdapter : ListAdapter<MyComment, MyCommentListViewHolder>(object :
    DiffUtil.ItemCallback<MyComment>() {
    override fun areItemsTheSame(oldItem: MyComment, newItem: MyComment): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: MyComment, newItem: MyComment): Boolean {
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
