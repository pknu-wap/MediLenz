package com.android.mediproject.feature.news

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun LazyPagingItems<*>.setOnStateChangedListener(
    block: @Composable (isFirstLoad: Boolean, isLoading: Boolean, isEmpty: Boolean) -> Unit,
) {
    val isFirstLoad: Boolean = loadState.refresh is LoadState.Loading
    val isLoading: Boolean = loadState.append is LoadState.Loading
    val isEmpty: Boolean = itemCount == 0

    block(isFirstLoad, isFirstLoad or isLoading, isEmpty)
}
