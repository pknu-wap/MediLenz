package com.android.mediproject.feature.search.result.manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.feature.search.databinding.ManualMedicineItemBinding
import com.bumptech.glide.Glide

class ApprovedMedicinesAdapter() : PagingDataAdapter<ApprovedMedicineItemDto, ApprovedMedicinesAdapter.ViewHolder>(MedicineComparator) {

    class ViewHolder(private val binding: ManualMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var item: ApprovedMedicineItemDto? = null

        init {
            binding.root.setOnClickListener {
                item?.apply {
                    onClick?.invoke(this)
                }
            }
        }

        fun bind(item: ApprovedMedicineItemDto) {
            binding.apply {
                this@ViewHolder.item = item

                medicineNameTextView.text = item.itemName
                ingredientTextView.text = item.itemIngrName
                manufacturerTextView.text = item.entpName
                effectTextView.text = item.spcltyPblc

                Glide.with(medicineImgView.context).load(item.bigPrdtImgUrl).into(medicineImgView)
            }
        }

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