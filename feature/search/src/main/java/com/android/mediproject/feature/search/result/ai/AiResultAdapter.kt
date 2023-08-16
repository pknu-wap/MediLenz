package com.android.mediproject.feature.search.result.ai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.feature.search.databinding.AiMedicineItemBinding

class AiResultAdapter : ListAdapter<ClassificationResult.Item, AiResultAdapter.ViewHolder>(MedicineComparator) {

    class ViewHolder(val binding: AiMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootLayout.setOnClickListener {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ClassificationResult.Item) {
            binding.apply {
                this.item = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int,
    ): ViewHolder = ViewHolder(AiMedicineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

internal object MedicineComparator : DiffUtil.ItemCallback<ClassificationResult.Item>() {
    override fun areItemsTheSame(
        oldItem: ClassificationResult.Item, newItem: ClassificationResult.Item,
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ClassificationResult.Item, newItem: ClassificationResult.Item,
    ): Boolean {
        return oldItem == newItem
    }
}
