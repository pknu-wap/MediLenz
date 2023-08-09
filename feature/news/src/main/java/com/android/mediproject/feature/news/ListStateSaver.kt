package com.android.mediproject.feature.news

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

@Composable
fun restoreListState(listState: LazyListState, listScrollState: State<Int>) {
    LaunchedEffect(Unit) {
        listState.scrollToItem(listScrollState.value)
    }
}

@Composable
fun rememberListState(listState: LazyListState, listScrollState: MutableState<Int>) {
    DisposableEffect(Unit) {
        onDispose {
            listScrollState.value = listState.firstVisibleItemIndex
        }
    }
}
