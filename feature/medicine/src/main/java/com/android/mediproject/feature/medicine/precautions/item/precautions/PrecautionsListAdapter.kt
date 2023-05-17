package com.android.mediproject.feature.medicine.precautions.item.precautions

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.feature.medicine.databinding.ItemViewPrecautionsBinding

class PrecautionsListAdapter : ListAdapter<Spanned, ViewHolder>(Diff) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemViewPrecautionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(private val binding: ItemViewPrecautionsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(text: Spanned) {
        binding.apply {
            this.precaution = text
            executePendingBindings()

        }
    }
}

object Diff : DiffUtil.ItemCallback<Spanned>() {
    override fun areItemsTheSame(oldItem: Spanned, newItem: Spanned): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Spanned, newItem: Spanned): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}