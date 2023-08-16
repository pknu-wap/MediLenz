package com.android.mediproject.feature.search.result.ai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.feature.search.databinding.AiMedicineItemBinding

class AiResultAdapter : ListAdapter<ClassificationResultEntity, AiResultAdapter.ViewHolder>(MedicineComparator) {

    class ViewHolder(val binding: AiMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootLayout.setOnClickListener {
                binding.item?.apply {
                    onClick?.invoke(medicineDetail!!)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ClassificationResultEntity) {
            binding.apply {
                this.item = item
                medicineNameTextView.text = "${item.classificationRecognitionEntity} - ${item.medicineDetail?.itemName}"
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

internal object MedicineComparator : DiffUtil.ItemCallback<ClassificationResultEntity>() {
    override fun areItemsTheSame(
        oldItem: ClassificationResultEntity, newItem: ClassificationResultEntity,
    ): Boolean {
        return oldItem.detectionObject == newItem.detectionObject
    }

    override fun areContentsTheSame(
        oldItem: ClassificationResultEntity, newItem: ClassificationResultEntity,
    ): Boolean {
        return oldItem == newItem
    }
}
