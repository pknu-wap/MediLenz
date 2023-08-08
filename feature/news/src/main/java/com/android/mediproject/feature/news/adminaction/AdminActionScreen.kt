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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.news.adminaction.AdminAction
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.ListItemScreen
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
            list[index]?.run {
                onClick = {
                    viewModel.onClickedItem(index)
                    navController.navigate("detailAdminAction")
                }
                ItemOnList(this)
            }
            if (index < list.itemCount - 1) Divider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        if (list.loadState.refresh == LoadState.Loading) {
            item {
                CenterProgressIndicator(stringResource(id = R.string.loadingAdminActionList))
            }
        }

    }
}

/**
 * 행정 처분 목록 아이템
 */
@Composable
fun ItemOnList(adminAction: AdminAction) {
    ListItemScreen(onClick = { adminAction.onClick?.invoke() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            // 업체명, 행정처분일자
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = adminAction.companyName,
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = adminAction.adminActionDate.value.format(dateFormat),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF6D6D6D),
                    ),
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            // 품목명
            Text(
                text = adminAction.itemName,
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF525050),
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 위반내용
            Text(
                text = adminAction.violationDetails,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF767272),
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 행정 처분명
            Text(
                text = adminAction.adminAction,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF808080),
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

}


private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
