package com.android.mediproject.feature.news.recallsuspension

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Preview
@Composable
fun RecallDisposalScreen(viewModel: RecallSuspensionViewModel = hiltViewModel()) {

    val items = viewModel.recallDisposalList.collectAsLazyPagingItems()

    when (items.loadState.refresh) {
        LoadState.Loading -> {
            //TODO implement loading state
        }

        is LoadState.Error -> {
            //TODO implement error state
        }

        else -> {
            LazyColumn(modifier = modifier) {
                itemsIndexed(books) { index, item ->
                    item?.let {
                        BookItem(
                            book = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp)
                        )
                    }
                }
            }
        }
    }


}