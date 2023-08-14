package com.android.mediproject.feature.camera.confirm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.ai.DetectionObject
import com.android.mediproject.feature.camera.databinding.ItemViewDetectedObjectBinding

class ImageListAdapter : ListAdapter<DetectionObject, ViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemViewDetectedObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(private val binding: ItemViewDetectedObjectBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.detectedObject?.onClick?.invoke()
        }
    }

    fun bind(detectedObject: DetectionObject) {
        binding.detectedObject = detectedObject
        binding.executePendingBindings()
    }
}

object Diff : DiffUtil.ItemCallback<DetectionObject>() {
    override fun areItemsTheSame(oldItem: DetectionObject, newItem: DetectionObject): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DetectionObject, newItem: DetectionObject): Boolean {
        return oldItem == newItem
    }
}
