package com.android.mediproject.feature.interestedmedicine.moreinterestedmedicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.interestedmedicine.MoreInterestedMedicineDto
import com.android.mediproject.feature.interestedmedicine.databinding.ItemInterestedMedicineBinding

class MoreInterestedMedicineViewHolder(private val binding: ItemInterestedMedicineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(moreInterestedMedicine: MoreInterestedMedicineDto) {
        binding.item = moreInterestedMedicine
    }
}

class MoreInterestedMeidicneAdapter :
    ListAdapter<MoreInterestedMedicineDto, MoreInterestedMedicineViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MoreInterestedMedicineDto>() {
            override fun areItemsTheSame(
                oldItem: MoreInterestedMedicineDto,
                newItem: MoreInterestedMedicineDto
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: MoreInterestedMedicineDto,
                newItem: MoreInterestedMedicineDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoreInterestedMedicineViewHolder {
        val binding = ItemInterestedMedicineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoreInterestedMedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoreInterestedMedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}