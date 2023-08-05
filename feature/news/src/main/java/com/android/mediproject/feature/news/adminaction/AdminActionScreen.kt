package com.android.mediproject.feature.news.adminaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.adminaction.AdminAction
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import java.time.format.DateTimeFormatter


/**
 * 행정 처분 목록 표시
 */
@Composable
fun AdminActionScreen() {
    val viewModel: AdminActionViewModel = hiltViewModel()
    val navController = rememberNavController()
    val list = viewModel.adminActionList.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            count = list.itemCount, key = list.itemKey(),
            contentType = list.itemContentType(
            ),
        ) { index ->
            list[index]?.let {
                it.onClick = {
                    viewModel.onClickedItem(index)
                    navController.navigate("detailAdminAction")
                }
                ListItem(it)
            }
            if (index < list.itemCount - 1) Divider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        when (list.loadState.append) {
            is LoadState.NotLoading -> item {
                CenterProgressIndicator(showOfItem = false)
            }

            is LoadState.Loading -> item {
                CenterProgressIndicator(showOfItem = true)
            }

            is LoadState.Error -> TODO()
            else -> TODO()
        }
    }
}

/**
 * 행정 처분 목록 아이템
 */
@Composable
fun ListItem(adminAction: AdminAction) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        shape = RectangleShape,
        onClick = {
            adminAction.onClick?.invoke()
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = adminAction.entpName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = adminAction.lastSettleDate.format(dateFormat),
                    fontSize = 12.sp,
                    modifier = Modifier.align(CenterVertically),
                    color = Color.Gray,
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = adminAction.violation, fontSize = 12.sp, color = Color.Gray, maxLines = 1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = adminAction.disposition, fontSize = 12.sp, color = Color.Gray, maxLines = 1,
            )
        }
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
