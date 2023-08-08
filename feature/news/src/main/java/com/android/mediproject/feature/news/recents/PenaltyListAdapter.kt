package com.android.mediproject.feature.news.recents

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.news.recall.RecallSaleSuspension
import com.android.mediproject.core.ui.base.view.SimpleListItemView
import com.android.mediproject.feature.news.R

class PenaltyListAdapter :
    ListAdapter<RecallSaleSuspension, PenaltyListAdapter.PenaltyViewHolder>(
        object :
            DiffUtil.ItemCallback<RecallSaleSuspension>() {
            override fun areItemsTheSame(
                oldItem: RecallSaleSuspension, newItem: RecallSaleSuspension,
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: RecallSaleSuspension, newItem: RecallSaleSuspension,
            ): Boolean = oldItem == newItem
        },
    ) {

    class PenaltyViewHolder(private val view: SimpleListItemView<RecallSaleSuspension>) :
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

        fun bind(data: RecallSaleSuspension) {
            view.apply {
                this.data = data
                setTitle(data.product)
                setContent(data.retrievalReason)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyViewHolder =
        PenaltyViewHolder(SimpleListItemView(parent.context, 0.6f))

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
