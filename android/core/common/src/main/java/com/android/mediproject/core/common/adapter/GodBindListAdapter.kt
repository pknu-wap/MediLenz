package com.android.mediproject.core.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * 귀찮음 줄여주는 ListAdapter
 *
 * @param T : ListAdapter에 들어갈 아이템 타입
 * @param V : ViewDataBinding
 * @param differ : DiffUtil.ItemCallback<T>
 *     - ListAdapter의 DiffUtil.ItemCallback
 *     - 아이템이 같은지 비교하는 메소드를 구현해야함
 * @param initViewHolder : (((CViewHolder<T, V>) -> Unit)
 *     - ViewHolder 초기화
 *     - ex) { holder -> ViewHolder class init 시 필요한 작업 처리 }
 * @param onBind : (CViewHolder<T, V>, T, Int) -> Unit
 *     - ViewHolder 바인딩
 *     - ex) { holder, item, position -> 필요한 작업 처리 }
 * @param itemViewId : Int
 *     - ViewHolder의 itemView id
 *     - ex) R.layout.item_view
 *
 */
abstract class GodBindListAdapter<T, V : ViewDataBinding>(
    differ: DiffUtil.ItemCallback<T>,
    private val initViewHolder: ((GodBindViewHolder<T, V>) -> Unit)?,
    private val onBind: (GodBindViewHolder<T, V>, T, Int) -> Unit,
    @IdRes private val itemViewId: Int
) : ListAdapter<T, GodBindViewHolder<T, V>>(differ) {

    private var _inflater: LayoutInflater? = null
    private val inflater get() = _inflater!!

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        _inflater = null
        _inflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GodBindViewHolder<T, V> =
        GodBindViewHolder(DataBindingUtil.inflate<V>(inflater, itemViewId, parent, false), initViewHolder, onBind)


    override fun onBindViewHolder(holder: GodBindViewHolder<T, V>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _inflater = null
    }
}

class GodBindViewHolder<T, V : ViewDataBinding>(
    private val binding: V,
    private val initViewHolder: ((GodBindViewHolder<T, V>) -> Unit)?,
    private val onBind: (GodBindViewHolder<T, V>, T, Int) -> Unit,
) : ViewHolder(binding.root) {

    init {
        initViewHolder?.invoke(this)
    }

    fun bind(item: T) {
        onBind.invoke(this, item, absoluteAdapterPosition)
        binding.executePendingBindings()
    }
}