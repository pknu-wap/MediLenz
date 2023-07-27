package com.android.mediproject.core.common.paging

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 * PagingDataAdapter의 상태 변화를 감지하여 로딩 상태에 따라 View를 보여주는 함수
 *
 * @param msgTextView 로딩 상태가 아닐 때 보여줄 메시지를 가진 TextView
 * @param listView RecyclerView
 * @param progressBar 로딩 상태일 때 보여줄 ProgressBar
 * @param emptyMsg 로딩 상태가 아닐 때 보여줄 메시지
 */
fun PagingDataAdapter<*, *>.setOnStateChangedListener(
    msgTextView: TextView,
    listView: RecyclerView,
    progressBar: CircularProgressIndicator,
    emptyMsg: String,
) {
    var isFirstLoad: Boolean
    addLoadStateListener { loadState ->
        isFirstLoad = loadState.refresh is LoadState.Loading
        if (isFirstLoad) listView.scrollToPosition(0)

        msgTextView.text = emptyMsg

        progressBar.isVisible = isFirstLoad
        listView.isVisible = (!isFirstLoad && (loadState.source.refresh !is LoadState.Loading))
        msgTextView.isVisible = ((!isFirstLoad && loadState.source.refresh !is LoadState.Loading) && itemCount == 0)
    }
}
