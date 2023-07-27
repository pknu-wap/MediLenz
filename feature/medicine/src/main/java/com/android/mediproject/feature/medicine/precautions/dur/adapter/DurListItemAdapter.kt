package com.android.mediproject.feature.medicine.precautions.dur.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.feature.medicine.databinding.ItemViewDurListItemBinding

class DurListItemAdapter(
    private val wrapper: DurListItemViewWrapper,
) : ListAdapter<DurItem, ItemInDurListItemViewHolder>(ItemInListItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInDurListItemViewHolder = ItemInDurListItemViewHolder(
        wrapper.getCacheView() ?: ItemViewDurListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: ItemInDurListItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()
}
