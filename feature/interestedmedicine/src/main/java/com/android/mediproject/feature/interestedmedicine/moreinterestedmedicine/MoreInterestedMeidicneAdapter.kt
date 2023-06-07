package com.android.mediproject.feature.interestedmedicine.moreinterestedmedicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineDto
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.feature.interestedmedicine.databinding.ItemInterestedMedicineBinding

class MoreInterestedMedicineViewHolder(private val binding: ItemInterestedMedicineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            root.setOnClickListener {
                item?.apply {
                    onClick?.invoke(this)
                }
            }
        }
    }

    fun bind(interestedMedicine: InterestedMedicineDto) {
        binding.item = interestedMedicine
    }
}

class MoreInterestedMeidicneAdapter :
    ListAdapter<InterestedMedicineDto, MoreInterestedMedicineViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<InterestedMedicineDto>() {
            override fun areItemsTheSame(
                oldItem: InterestedMedicineDto,
                newItem: InterestedMedicineDto
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: InterestedMedicineDto,
                newItem: InterestedMedicineDto
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