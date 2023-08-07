package com.android.mediproject.feature.news.recallsalesuspension

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
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
import com.android.mediproject.core.model.recall.RecallSuspension
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import java.time.format.DateTimeFormatter


/**
 * 회수 폐기 목록 표시
 */
@Composable
fun RecallDisposalScreen(
) {
    val viewModel: RecallSuspensionViewModel = hiltViewModel()
    val navController = rememberNavController()
    val list = viewModel.recallDisposalList.collectAsLazyPagingItems()

    when (list.loadState.append) {
        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                items(
                    count = list.itemCount, key = list.itemKey(),
                    contentType = list.itemContentType(
                    ),
                ) { index ->
                    list[index]?.run {
                        onClick = {
                            navController.navigate("detailRecallSuspension/${it.product}")
                        }
                        ListItem(this)
                    }
                    if (index < list.itemCount - 1) Divider(modifier = Modifier.padding(horizontal = 24.dp))
                }
            }
        }

        is LoadState.Loading -> {
            CenterProgressIndicator(stringResource(id = R.string.loadingRecallSaleSuspensionData))
        }

        else -> TODO()
    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItem(recallSuspension: RecallSuspension) {
    Surface(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            recallSuspension.onClick?.invoke(recallSuspension)
        },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            val (image, product, reason, entp, date) = createRefs()
            createVerticalChain(product, reason, date, chainStyle = ChainStyle.Packed)
            val barrier = createEndBarrier(image, margin = 11.dp)

            Image(
                painter = painterResource(id = com.android.mediproject.core.ui.R.drawable.baseline_camera_24),
                contentDescription = stringResource(id = R.string.recallSaleSuspensionImage),
                modifier = Modifier
                    .width(100.dp)
                    .height(75.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
            )

            // 품목명
            Text(
                text = recallSuspension.product,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(product) {
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
                text = recallSuspension.reason,
                textAlign = TextAlign.Left,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(reason) {
                    top.linkTo(product.bottom, 8.dp)
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
                text = recallSuspension.entrps,
                textAlign = TextAlign.Right,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(entp) {
                    start.linkTo(barrier)
                    baseline.linkTo(date.baseline)
                    end.linkTo(date.start, 9.dp)
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
                text = if (recallSuspension.recallCommandDate.isEmpty) recallSuspension.destructionOrderDate.value.toString() else recallSuspension.recallCommandDate.value.toString(),
                textAlign = TextAlign.Right,
                modifier = Modifier.constrainAs(date) {
                    top.linkTo(reason.bottom, 6.dp)
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


private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")


/**
Column(
modifier = Modifier.fillMaxWidth(),
) {
Row(
horizontalArrangement = Arrangement.spacedBy(8.dp),
verticalAlignment = CenterVertically,
) {
Text(
text = recallSuspension.product,
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
text = recallSuspension.run {
if (recallCommandDate.isEmpty) recallCommandDate
else destructionOrderDate
}.value.format(dateFormat),
fontSize = 12.sp,
modifier = Modifier.align(CenterVertically),
color = Color.Gray,
maxLines = 1,
)
}
Spacer(modifier = Modifier.height(8.dp))
Text(
text = recallSuspension.reason, fontSize = 12.sp, color = Color.Gray, maxLines = 1,
)
}
 */
