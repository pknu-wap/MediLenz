package com.android.mediproject.feature.comments.mycommentslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.MyComment
import com.android.mediproject.feature.comments.databinding.ItemMyCommentBinding

class MyCommentsViewHolder(
    private val binding: ItemMyCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            root.setOnClickListener {
                myComment?.apply {
                    onClick?.invoke(this)
                }
            }
        }
    }

    fun bind(myComment: MyComment) {
        binding.myComment = myComment
    }
}


class MyCommentsListAdapter : ListAdapter<MyComment, MyCommentsViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyComment>() {
            override fun areItemsTheSame(oldItem: MyComment, newItem: MyComment): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MyComment, newItem: MyComment): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentsViewHolder {
        val binding =
            ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<MyComment>?) {
        super.submitList(list)
    }
}
