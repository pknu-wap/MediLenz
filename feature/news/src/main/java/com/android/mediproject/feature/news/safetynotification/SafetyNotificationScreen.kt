package com.android.mediproject.feature.news.safetynotification

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.news.safetynotification.SafetyNotification
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.ListItemScreen
import com.android.mediproject.feature.news.rememberListState
import com.android.mediproject.feature.news.restoreListState


@Composable
fun SafetyNotificationScreen() {
    val viewModel: SafetyNotificationViewModel = hiltViewModel()
    val navController = rememberNavController()
    val list = viewModel.safetyNotificationList.collectAsLazyPagingItems()

    val listState = rememberLazyListState()
    restoreListState(listState = listState, listScrollState = viewModel.listScrollState)

    LazyColumn(
        state = listState
    ) {
        items(
            count = list.itemCount, key = list.itemKey(),
            contentType = list.itemContentType(
            ),
        ) { index ->
            list[index]?.run {
                onClick = {
                    navController.navigate("detailAdminAction")
                }
                ItemOnList(this)
            }
            if (index < list.itemCount - 1) Divider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        if (list.loadState.refresh == LoadState.Loading) {
            item {
                CenterProgressIndicator(stringResource(id = R.string.loadingSafetyNotification))
            }
        }
    }

    rememberListState(listState = listState, listScrollState = viewModel.listScrollState)
}


@Composable
fun ItemOnList(safetyNotification: SafetyNotification) {
    ListItemScreen(onClick = { safetyNotification.onClick?.invoke(safetyNotification) }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            val (title, info, date) = createRefs()
            createVerticalChain(title, info, chainStyle = ChainStyle.Packed)

            // 제목
            Text(
                text = safetyNotification.title,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(date.start)
                    bottom.linkTo(info.top)
                    width = Dimension.fillToConstraints
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                ),
            )

            // 정보
            Text(
                text = safetyNotification.informationSummary,
                textAlign = TextAlign.Left,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(title.bottom, 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF525050),
                ),
            )

            // 날짜
            Text(
                text = safetyNotification.publicationDate.value.toString(),
                textAlign = TextAlign.Right,
                modifier = Modifier.constrainAs(date) {
                    start.linkTo(title.end, 8.dp)
                    end.linkTo(parent.end)
                    baseline.linkTo(title.baseline)
                },
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF6D6D6D),
                ),
            )
        }
    }
}
