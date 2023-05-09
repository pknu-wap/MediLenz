package com.android.mediproject.feature.news.recallsuspension

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun RecallDisposalScreen(viewModel: RecallSuspensionViewModel = hiltViewModel()) {

    val list = viewModel.recallDisposalList.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            count = list.itemCount, key = list.itemKey(), contentType = list.itemContentType(
            )
        ) { index ->
            list[index]?.let { ListItem(it) }
            if (index < list.itemCount - 1) Divider(color = Black, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
        }

        when (list.loadState.append) {
            is LoadState.NotLoading -> Unit
            is LoadState.Loading -> {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> TODO()
            else -> TODO()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(recallSuspensionListItemDto: RecallSuspensionListItemDto) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RectangleShape,
        onClick = {
            recallSuspensionListItemDto.onClick?.invoke(recallSuspensionListItemDto)
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Text(
                    text = recallSuspensionListItemDto.product,
                    style = MaterialTheme.typography.titleMedium, fontSize = 14.sp,
                    color = Color.Black,
                )
                Text(
                    text = recallSuspensionListItemDto.let {
                        if (it.recallCommandDate != null) it.recallCommandDate
                        else it.rtrlCommandDt
                    }!!.toJavaLocalDate().format(dateFormat),
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recallSuspensionListItemDto.rtrvlResn, fontSize = 12.sp,
                color = Color.Gray,
            )
        }
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")