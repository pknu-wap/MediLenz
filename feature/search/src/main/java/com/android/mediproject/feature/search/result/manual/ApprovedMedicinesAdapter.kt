package com.android.mediproject.feature.search.result.manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.feature.search.databinding.ManualMedicineItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ApprovedMedicinesAdapter() : PagingDataAdapter<ApprovedMedicineItemDto, ApprovedMedicinesAdapter.ViewHolder>(MedicineComparator) {

    class ViewHolder(private val binding: ManualMedicineItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var item: ApprovedMedicineItemDto? = null

        private val glide: RequestManager = Glide.with(binding.root)

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
                medicineTypeTextView.text = item.spcltyPblc

                // 메모리 캐시 사용
                glide.load(item.bigPrdtImgUrl)
                    .override(medicineImgView.width).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
                    .into(medicineImgView)
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