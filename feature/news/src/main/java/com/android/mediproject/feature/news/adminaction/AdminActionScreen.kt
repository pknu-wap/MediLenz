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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.remote.adminaction.AdminActionListItemDto
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import java.time.format.DateTimeFormatter


/**
 * 행정 처분 목록 표시
 */
@Composable
fun AdminActionScreen(
    viewModel: AdminActionViewModel = hiltViewModel(), navController: NavController
) {
    val list = viewModel.adminActionList.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            count = list.itemCount, key = list.itemKey(), contentType = list.itemContentType(
            )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(adminActionListItemDto: AdminActionListItemDto) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 9.dp),
        shape = RectangleShape,
        onClick = {
            adminActionListItemDto.onClick?.invoke()
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = adminActionListItemDto.entpName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = adminActionListItemDto.lastSettleDate.format(dateFormat),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.Gray,
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = adminActionListItemDto.violation, fontSize = 12.sp, color = Color.Gray, maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = adminActionListItemDto.disposition, fontSize = 12.sp, color = Color.Gray, maxLines = 1
            )
        }
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")