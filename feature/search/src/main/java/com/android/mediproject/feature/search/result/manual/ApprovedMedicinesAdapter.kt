package com.android.mediproject.feature.search.result.manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.bindingadapter.GlideApp
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.feature.search.databinding.ManualMedicineItemBinding

class ApprovedMedicinesAdapter() : PagingDataAdapter<ApprovedMedicineItemDto, ApprovedMedicinesAdapter.ViewHolder>(MedicineComparator) {

    init {
        setHasStableIds(true)
    }

    class ViewHolder(val binding: ManualMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.item?.apply {
                    onClick?.invoke(this)
                }
            }
        }

        fun bind(item: ApprovedMedicineItemDto) {
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
        parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder(ManualMedicineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }
}

object MedicineComparator : DiffUtil.ItemCallback<ApprovedMedicineItemDto>() {
    override fun areItemsTheSame(
        oldItem: ApprovedMedicineItemDto, newItem: ApprovedMedicineItemDto
    ): Boolean {
        return oldItem.itemSeq == newItem.itemSeq
    }

    override fun areContentsTheSame(
        oldItem: ApprovedMedicineItemDto, newItem: ApprovedMedicineItemDto
    ): Boolean {
        return oldItem == newItem
    }
}