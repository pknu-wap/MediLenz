package com.android.mediproject.feature.medicine.precautions.dur.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.recyclerview.MListAdapter
import com.android.mediproject.core.model.dur.DurListItem
import com.android.mediproject.feature.medicine.databinding.ItemViewDurListBinding

class DurListAdapter : MListAdapter<DurListItem, DurListAdapter.ViewHolder>(ListItemCallback()) {

    private val itemViewPool = RecyclerView.RecycledViewPool()
    private var _itemViewWrapper: DurListItemViewWrapper? = null
    private val itemViewWrapper: DurListItemViewWrapper
        get() = _itemViewWrapper!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (_itemViewWrapper == null) _itemViewWrapper = DurListItemViewWrapper(itemCount, parent.context)
        return ViewHolder(ItemViewDurListBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemViewPool, itemViewWrapper)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _itemViewWrapper?.release()
        _itemViewWrapper = null
        itemViewPool.clear()
    }

    class ViewHolder(
        private val binding: ItemViewDurListBinding,
        viewPool: RecyclerView.RecycledViewPool,
        itemViewWrapper: DurListItemViewWrapper,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val contentsAdapter = DurListItemAdapter(itemViewWrapper)

        init {
            binding.contentsRecyclerView.apply {
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
                adapter = contentsAdapter
            }
        }

        fun onBind(item: DurListItem) {
            item.durItems?.onSuccess { contentsAdapter.submitList(it) }?.onFailure { }
        }
    }


}
