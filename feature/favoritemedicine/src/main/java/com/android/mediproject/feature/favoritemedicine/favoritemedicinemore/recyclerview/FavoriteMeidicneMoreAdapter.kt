package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineMoreDto
import com.android.mediproject.feature.interestedmedicine.databinding.ItemFavoriteMedicineBinding

class FavoriteMedicineMoreViewHolder(private val binding: ItemFavoriteMedicineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(moreInterestedMedicine: FavoriteMedicineMoreDto) {
        binding.item = moreInterestedMedicine
    }
}

class FavoriteMeidicneMoreAdapter :
    ListAdapter<FavoriteMedicineMoreDto, FavoriteMedicineMoreViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FavoriteMedicineMoreDto>() {
            override fun areItemsTheSame(
                oldItem: FavoriteMedicineMoreDto,
                newItem: FavoriteMedicineMoreDto
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteMedicineMoreDto,
                newItem: FavoriteMedicineMoreDto
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