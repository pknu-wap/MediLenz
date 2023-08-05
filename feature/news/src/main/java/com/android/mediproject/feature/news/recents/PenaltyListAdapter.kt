package com.android.mediproject.feature.news.recents

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.recall.RecallSuspension
import com.android.mediproject.core.ui.base.view.SimpleListItemView
import com.android.mediproject.feature.news.R

class PenaltyListAdapter :
    ListAdapter<RecallSuspension, PenaltyListAdapter.PenaltyViewHolder>(
        object :
            DiffUtil.ItemCallback<RecallSuspension>() {
            override fun areItemsTheSame(
                oldItem: RecallSuspension, newItem: RecallSuspension,
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: RecallSuspension, newItem: RecallSuspension,
            ): Boolean = oldItem == newItem
        },
    ) {

    class PenaltyViewHolder(private val view: SimpleListItemView<RecallSuspension>) :
        RecyclerView.ViewHolder(view) {
        init {
            view.apply {
                setTitleColor(view.context.resources.getColor(R.color.newsChipColor, null))
                setContentTextColor(view.context.resources.getColor(R.color.newsContentColor, null))
                setChipStrokeColor(R.color.newsChipColor)
                setOnItemClickListener {
                    it?.apply {
                        onClick?.invoke(this)
                    }
                }
            }
        }

        fun bind(data: RecallSuspension) {
            view.apply {
                this.data = data
                setTitle(data.product)
                setContent(data.reason)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyViewHolder =
        PenaltyViewHolder(SimpleListItemView(parent.context, 0.6f))

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
