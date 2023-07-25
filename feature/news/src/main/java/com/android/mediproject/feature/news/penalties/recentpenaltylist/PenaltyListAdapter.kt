package com.android.mediproject.feature.news.penalties.recentpenaltylist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.view.SimpleListItemView
import com.android.mediproject.feature.news.R

class PenaltyListAdapter :
    ListAdapter<RecallSuspensionListItemDto, PenaltyListAdapter.PenaltyViewHolder>(object :
        DiffUtil.ItemCallback<RecallSuspensionListItemDto>() {
        override fun areItemsTheSame(
            oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RecallSuspensionListItemDto, newItem: RecallSuspensionListItemDto
        ): Boolean = oldItem == newItem
    }) {

    class PenaltyViewHolder(private val view: SimpleListItemView<RecallSuspensionListItemDto>) :
        RecyclerView.ViewHolder(view) {

        init {
            view.setTitleColor(view.context.resources.getColor(R.color.newsChipColor, null))
            view.setContentTextColor(view.context.resources.getColor(R.color.newsContentColor, null))
            view.setChipStrokeColor(R.color.newsChipColor)

            view.setOnItemClickListener {
                it?.apply {
                    onClick?.invoke(this)
                }
            }
        }

        fun bind(data: RecallSuspensionListItemDto) {
            view.data = data
            view.setTitle(data.product)
            view.setContent(data.reason)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyViewHolder =
        PenaltyViewHolder(SimpleListItemView(parent.context, 0.6f))

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
