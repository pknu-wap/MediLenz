package com.android.mediproject.feature.camera.confirmdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.feature.camera.aimodel.DetectedObject
import com.android.mediproject.feature.camera.databinding.ItemViewDetectedObjectBinding

class ImageListAdapter : ListAdapter<DetectedObject, ViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemViewDetectedObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}

class ViewHolder(private val binding: ItemViewDetectedObjectBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.detectedObject?.let { item ->
                item.onClicked?.invoke()
            }
        }
    }

    fun bind(detectedObject: DetectedObject) {
        binding.detectedObject = detectedObject
        binding.detectedObjectImageView.setImageBitmap(detectedObject.bitmap)
    }
}

object Diff : DiffUtil.ItemCallback<DetectedObject>() {
    override fun areItemsTheSame(oldItem: DetectedObject, newItem: DetectedObject): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DetectedObject, newItem: DetectedObject): Boolean {
        return oldItem == newItem
    }
}