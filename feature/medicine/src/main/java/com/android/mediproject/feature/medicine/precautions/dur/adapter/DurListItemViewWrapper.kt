package com.android.mediproject.feature.medicine.precautions.dur.adapter

import android.content.Context
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.ItemViewDurListItemBinding
import java.util.Stack

class DurListItemViewWrapper(durCount: Int, context: Context) {
    private val viewCache = Stack<ItemViewDurListItemBinding>()

    init {
        AsyncLayoutInflater(context).run {
            repeat(durCount + durCount / 2) {
                inflate(R.layout.item_view_dur_list_item, null) { view, _, _ ->
                    synchronized(viewCache) {
                        viewCache.push(
                            ItemViewDurListItemBinding.bind(view),
                        )
                    }
                }
            }
        }
    }

    fun getCacheView(): ItemViewDurListItemBinding? = synchronized(viewCache) {
        if (viewCache.isEmpty()) null
        else viewCache.pop()
    }

    fun release() {
        viewCache.clear()
    }
}
