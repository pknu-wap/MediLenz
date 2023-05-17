package com.android.mediproject.feature.penalties.recentpenaltylist

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.view.SimpleListItemView

class PenaltyListAdapter : ListAdapter<RecallSuspensionListItemDto, PenaltyListAdapter.PenaltyViewHolder>(Diff) {

    class PenaltyViewHolder(private val view: SimpleListItemView<RecallSuspensionListItemDto>) : RecyclerView.ViewHolder(view) {

        private var item: RecallSuspensionListItemDto? = null

        init {
            view.setOnItemClickListener {
                it?.apply {
                    onClick?.invoke(this)
                }
            }

        }

        fun bind(data: RecallSuspensionListItemDto) {
            item = data
            view.setTitle(data.product)
            view.setContent(data.rtrvlResn)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyViewHolder =
        PenaltyViewHolder(SimpleListItemView<RecallSuspensionListItemDto>(parent.context).apply {
            setTitleColor(Color.BLACK)
            setContentTextColor(Color.BLACK)
            setChipStrokeColor(Color.BLACK)
        })

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

object Diff : DiffUtil.ItemCallback<RecallSuspensionListItemDto>() {
    override fun areItemsTheSame(oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto): Boolean =
        oldItem == newItem
}