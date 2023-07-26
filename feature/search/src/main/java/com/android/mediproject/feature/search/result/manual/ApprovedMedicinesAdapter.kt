package com.android.mediproject.feature.search.result.manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.bindingadapter.GlideApp
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicine
import com.android.mediproject.feature.search.databinding.ManualMedicineItemBinding

class ApprovedMedicinesAdapter : PagingDataAdapter<ApprovedMedicine, ApprovedMedicinesAdapter.ViewHolder>(MedicineComparator) {


    class ViewHolder(val binding: ManualMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.item?.apply {
                    onClick?.invoke(this)
                }
            }
        }

        fun bind(item: ApprovedMedicine) {
            binding.apply {
                this.item = item
                executePendingBindings()
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        GlideApp.with(holder.itemView.context).clear(holder.binding.medicineImgView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int,
    ): ViewHolder = ViewHolder(ManualMedicineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}

object MedicineComparator : DiffUtil.ItemCallback<ApprovedMedicine>() {
    override fun areItemsTheSame(
        oldItem: ApprovedMedicine, newItem: ApprovedMedicine,
    ): Boolean {
        return oldItem.itemSeq == newItem.itemSeq
    }

    override fun areContentsTheSame(
        oldItem: ApprovedMedicine, newItem: ApprovedMedicine,
    ): Boolean {
        return oldItem == newItem
    }
}
