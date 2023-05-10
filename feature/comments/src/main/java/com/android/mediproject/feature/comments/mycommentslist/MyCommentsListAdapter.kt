package com.android.mediproject.feature.comments.mycommentslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.feature.comments.databinding.ItemMyCommnetBinding

class MyCommentsViewHolder(
    private val binding: ItemMyCommnetBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            root.setOnClickListener {
                myCommnet?.apply {
                    onClick?.invoke(this)
                }
            }
        }
    }

    fun bind(myComment: MyCommentDto) {
        binding.myCommnet = myComment
    }
}


class MyCommentsListAdapter : ListAdapter<MyCommentDto, MyCommentsViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyCommentDto>() {
            override fun areItemsTheSame(oldItem: MyCommentDto, newItem: MyCommentDto): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MyCommentDto, newItem: MyCommentDto): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentsViewHolder {
        val binding =
            ItemMyCommnetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<MyCommentDto>?) {
        super.submitList(list)
    }
}