package com.android.mediproject.core.common.recyclerview

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

abstract class MListAdapter<T, VH : RecyclerView.ViewHolder> : ListAdapter<T, VH> {
    private var init = true
    private var iView: IView? = null

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)
    constructor(config: AsyncDifferConfig<T>) : super(config)

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        super.onCurrentListChanged(previousList, currentList)
        iView?.run {
            Log.d(
                "MListAdapter", "previousListEmpty : ${previousList.isEmpty()}, currentListEmpty : ${currentList.isEmpty()}",
            )

            msgTextViewIsVisible(if (!init and previousList.isEmpty() and currentList.isEmpty()) View.VISIBLE else View.GONE)
            loadTextViewIsVisible(if (init) View.VISIBLE else View.GONE)
            progressBarIsVisible(if (init) View.VISIBLE else View.GONE)
            listViewIsVisible(if (currentList.isNotEmpty()) View.VISIBLE else View.GONE)

            if (previousList.isEmpty() and currentList.isNotEmpty()) listViewScrollToPosition(0)
            init = false
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
        val msgTextViewIsVisible: (Int) -> Unit = msgTextView::setVisibility
        val loadTextViewIsVisible: (Int) -> Unit = loadTextView::setVisibility
        val progressBarIsVisible: (Int) -> Unit = progressBar::setVisibility
        val listViewIsVisible: (Int) -> Unit = listView::setVisibility
        val listViewScrollToPosition: (Int) -> Unit = listView::scrollToPosition

        init {
            msgTextView.text = emptyMsg
            loadTextView.text = loadMsg
        }
    }
}
