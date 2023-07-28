package com.android.mediproject.feature.medicine.precautions.dur.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.core.model.dur.DurListItem
import com.android.mediproject.feature.medicine.databinding.ItemViewDurListItemBinding


class ItemInListItemCallback : DiffUtil.ItemCallback<DurItem>() {
    override fun areContentsTheSame(oldItem: DurItem, newItem: DurItem): Boolean = oldItem.content.contentEquals(newItem.content)

    override fun areItemsTheSame(oldItem: DurItem, newItem: DurItem): Boolean = oldItem == newItem
}


class ListItemCallback : DiffUtil.ItemCallback<DurListItem>() {
    override fun areItemsTheSame(oldItem: DurListItem, newItem: DurListItem): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: DurListItem, newItem: DurListItem): Boolean = oldItem.durType == newItem.durType
}


class ItemInDurListItemViewHolder(
    private val binding: ItemViewDurListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(durItem: DurItem) {
        binding.contentTextView.text = durItem.content
    }
}
