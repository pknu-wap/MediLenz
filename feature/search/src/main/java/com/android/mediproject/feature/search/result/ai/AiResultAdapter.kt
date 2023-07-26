package com.android.mediproject.feature.search.result.ai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.feature.search.databinding.AiMedicineItemBinding

class AiResultAdapter : ListAdapter<ClassificationResult, AiResultAdapter.ViewHolder>(MedicineComparator) {

    class ViewHolder(val binding: AiMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootLayout.setOnClickListener {
                binding.item?.apply {
                    onClick?.invoke(medicineDetailInfo!!)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ClassificationResult) {
            binding.apply {
                this.item = item
                medicineNameTextView.text = "${item.classificationRecognition} - ${item.medicineDetailInfo?.itemName}"
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

internal object MedicineComparator : DiffUtil.ItemCallback<ClassificationResult>() {
    override fun areItemsTheSame(
        oldItem: ClassificationResult, newItem: ClassificationResult,
    ): Boolean {
        return oldItem.detectionObject == newItem.detectionObject
    }

    override fun areContentsTheSame(
        oldItem: ClassificationResult, newItem: ClassificationResult,
    ): Boolean {
        return oldItem == newItem
    }
}
