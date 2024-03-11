package com.android.mediproject.feature.news

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun CoroutineScope.RestoreListState(listState: LazyListState, listScrollState: Pair<Int, Int>) {
    LaunchedEffect(Unit) {
        listScrollState.let { (index, offset) ->
            // listState.scrollToItem(index, offset)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            //listScrollState.value = listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
    }
}
