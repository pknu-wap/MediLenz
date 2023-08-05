package com.android.mediproject.feature.search.result.manual

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.android.mediproject.core.ui.base.view.LoadStateViewHolder
import com.android.mediproject.core.ui.base.view.LoadingState

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = when (loadState) {
        is LoadState.Loading -> holder.setLoadingState(LoadingState.Loading)
        is LoadState.Error -> holder.setLoadingState(LoadingState.Error(loadState.error.message ?: ""))
        is LoadState.NotLoading -> holder.setLoadingState(LoadingState.NotLoading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder(parent.context, retry)
}