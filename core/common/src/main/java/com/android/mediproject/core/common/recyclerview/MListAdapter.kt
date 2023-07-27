package com.android.mediproject.core.common.recyclerview

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlin.reflect.KProperty

abstract class MListAdapter<T, VH : RecyclerView.ViewHolder> : ListAdapter<T, VH> {

    private var iView: IView? = null

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)
    constructor(config: AsyncDifferConfig<T>) : super(config)

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        super.onCurrentListChanged(previousList, currentList)
        iView?.run {
            val isFirstLoad = previousList.isEmpty() && currentList.isNotEmpty()

            progressBarIsVisible.call(isFirstLoad)
            loadTextViewIsVisible.call(isFirstLoad)
            msgTextViewIsVisible.call(previousList.isEmpty() && currentList.isEmpty())
            listViewIsVisible.call(currentList.isNotEmpty())

            if (previousList.isEmpty() && currentList.isNotEmpty()) listViewScrollToPosition(0)
        }
    }

    fun setOnStateChangedListener(
        msgTextView: TextView,
        loadTextView: TextView,
        listView: RecyclerView,
        progressBar: CircularProgressIndicator,
        emptyMsg: String,
        loadMsg: String,
    ) {
        iView = IView(msgTextView, loadTextView, listView, progressBar, emptyMsg, loadMsg)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        iView = null
    }

    private class IView(
        msgTextView: TextView,
        loadTextView: TextView,
        listView: RecyclerView,
        progressBar: CircularProgressIndicator,
        emptyMsg: String,
        loadMsg: String,
    ) {
        val msgTextViewIsVisible: KProperty<Boolean> = msgTextView::isVisible
        val loadTextViewIsVisible: KProperty<Boolean> = loadTextView::isVisible
        val progressBarIsVisible: KProperty<Boolean> = progressBar::isVisible
        val listViewIsVisible: KProperty<Boolean> = listView::isVisible
        val listViewScrollToPosition: (Int) -> Unit = listView::scrollToPosition

        init {
            msgTextView.text = emptyMsg
            loadTextView.text = loadMsg
        }
    }
}
