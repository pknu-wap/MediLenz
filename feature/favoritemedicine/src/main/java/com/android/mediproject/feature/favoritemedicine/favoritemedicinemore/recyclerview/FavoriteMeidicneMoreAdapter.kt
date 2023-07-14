package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.interestedmedicine.MoreInterestedMedicineDto
import com.android.mediproject.feature.interestedmedicine.databinding.ItemFavoriteMedicineBinding

class FavoriteMedicineMoreViewHolder(private val binding: ItemFavoriteMedicineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(moreInterestedMedicine: MoreInterestedMedicineDto) {
        binding.item = moreInterestedMedicine
    }
}

class FavoriteMeidicneMoreAdapter :
    ListAdapter<MoreInterestedMedicineDto, FavoriteMedicineMoreViewHolder>(diffUtil) {

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
    ): FavoriteMedicineMoreViewHolder {
        val binding = ItemFavoriteMedicineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteMedicineMoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMedicineMoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}