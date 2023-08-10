package com.android.mediproject.feature.news.recallsalesuspension

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.mediproject.core.model.news.recall.RecallSaleSuspension
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.ListItemScreen
import com.android.mediproject.feature.news.listDateTimeFormat
import com.android.mediproject.feature.news.rememberListState
import com.android.mediproject.feature.news.restoreListState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


/**
 * 회수 폐기 목록 표시
 */
@Composable
fun RecallSaleSuspensionScreen(
    onClicked: (product: String) -> Unit,
) {
    val viewModel: RecallSuspensionViewModel = hiltViewModel()
    val list = viewModel.recallDisposalList.collectAsLazyPagingItems()

    val listState = rememberLazyListState()
    restoreListState(listState = listState, listScrollState = viewModel.listScrollState)

    LazyColumn(state = listState) {
        items(
            count = list.itemCount, key = list.itemKey(),
            contentType = list.itemContentType(
            ),
        ) { index ->
            list[index]?.run {
                onClick = {
                    onClicked(product)
                }
                ItemOnList(this)
            }
            if (index < list.itemCount - 1) Divider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        if (list.loadState.refresh == LoadState.Loading) {
            item {
                CenterProgressIndicator(stringResource(id = R.string.loadingRecallSaleSuspensionData))
            }
        }
    }

    rememberListState(listState = listState, listScrollState = viewModel.listScrollState)
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemOnList(recallSaleSuspension: RecallSaleSuspension) {
    ListItemScreen(onClick = { recallSaleSuspension.onClick?.invoke(recallSaleSuspension) }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            val (image, product, reason, entp, date) = createRefs()
            createVerticalChain(product, reason, date, chainStyle = ChainStyle.Packed)
            val barrier = createEndBarrier(image, margin = 11.dp)

            GlideImage(
                model = recallSaleSuspension.imageUrl,
                contentDescription = stringResource(id = R.string.recallSaleSuspensionImage),
                alignment = Alignment.Center,
                modifier = Modifier
                    .width(90.dp)
                    .height(75.dp)
                    .padding(bottom = 12.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
            )

            // 품목명
            Text(
                text = recallSaleSuspension.product,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier
                    .constrainAs(product) {
                        top.linkTo(parent.top)
                        start.linkTo(barrier)
                        end.linkTo(parent.end)
                        bottom.linkTo(reason.top)
                        width = Dimension.fillToConstraints
                    },
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                ),
            )

            // 회수 사유
            Text(
                text = recallSaleSuspension.retrievalReason,
                textAlign = TextAlign.Left,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier
                    .constrainAs(reason) {
                        top.linkTo(product.bottom, 12.dp)
                        start.linkTo(barrier)
                        end.linkTo(parent.end)
                        bottom.linkTo(date.top)
                        width = Dimension.fillToConstraints
                    },
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF808080),
                ),
            )

            // 업체명
            Text(
                text = recallSaleSuspension.company,
                textAlign = TextAlign.Right,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(entp) {
                    start.linkTo(barrier)
                    baseline.linkTo(date.baseline)
                    end.linkTo(date.start, 8.dp)
                    width = Dimension.fillToConstraints

                },
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF6D6D6D),
                ),
            )

            // 날짜
            Text(
                text = if (recallSaleSuspension.recallCommandDate.isEmpty) recallSaleSuspension.retrievalCommandDate.value.format(listDateTimeFormat) else recallSaleSuspension.recallCommandDate.value.format(
                    listDateTimeFormat,
                ),
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(reason.bottom, 10.dp)
                        start.linkTo(entp.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
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
