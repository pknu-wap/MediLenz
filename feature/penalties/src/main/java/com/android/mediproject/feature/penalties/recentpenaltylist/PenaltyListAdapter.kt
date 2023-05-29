package com.android.mediproject.feature.penalties.recentpenaltylist

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.view.SimpleListItemView

class PenaltyListAdapter : ListAdapter<RecallSuspensionListItemDto, PenaltyListAdapter.PenaltyViewHolder>(object :
    DiffUtil.ItemCallback<RecallSuspensionListItemDto>() {
    override fun areItemsTheSame(oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto): Boolean =
        oldItem == newItem
}) {

    class PenaltyViewHolder(private val view: SimpleListItemView<RecallSuspensionListItemDto>) : RecyclerView.ViewHolder(view) {

        private var item: RecallSuspensionListItemDto? = null

        init {
            view.setTitleColor(Color.BLACK)
            view.setContentTextColor(Color.BLACK)
            view.setChipStrokeColor(android.R.color.black)

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
        PenaltyViewHolder(SimpleListItemView<RecallSuspensionListItemDto>(parent.context))

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}