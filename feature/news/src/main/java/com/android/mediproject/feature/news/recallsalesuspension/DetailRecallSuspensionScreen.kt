package com.android.mediproject.feature.news.recallsalesuspension

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.mediproject.core.common.viewmodel.onError
import com.android.mediproject.core.common.viewmodel.onInitial
import com.android.mediproject.core.common.viewmodel.onLoading
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.CardBox
import com.android.mediproject.feature.news.customui.Header

@Composable
fun DetailRecallDisposalScreen(
) {
    val viewModel: DetailRecallSuspensionViewModel = hiltViewModel()
    val navController: NavController = rememberNavController()

    val uiState = viewModel.detailRecallSuspension.collectAsStateWithLifecycle()

    uiState.value.onInitial {

    }.onError { error -> }.onLoading {
        CenterProgressIndicator(stringResource(id = R.string.loadingRecallSaleSuspensionData))
    }.onSuccess { detailRecallSuspension ->
        //Item(item = detailRecallSuspension)
    }

}

// 회수 폐기
@Composable
@Preview(showBackground = true)
fun Item() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RectangleShape,
    ) {
        Column {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (header, product, image) = createRefs()

                // 제품명
                Header(
                    text = stringResource(id = R.string.productName),
                    modifier = Modifier.constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(product.top)
                    },
                )

                Text(
                    text = "노게스타정1.5밀리그램(레보노르게스트렐)",
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(product) {
                        start.linkTo(parent.start)
                        end.linkTo(image.start, 12.dp)
                        top.linkTo(header.bottom, 6.dp)
                        width = Dimension.fillToConstraints
                    },
                )

                Image(
                    painter = painterResource(id = com.android.mediproject.core.ui.R.drawable.baseline_camera_24),
                    contentDescription = stringResource(id = R.string.recallSaleSuspensionImage),
                    modifier = Modifier
                        .width(100.dp)
                        .height(75.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    alignment = Alignment.Center,
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                )
            }


            Spacer(modifier = Modifier.height(6.dp))

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                // 업체명
                Header(text = stringResource(id = R.string.companyName))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "(주)다산제약",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF32649F),
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 회수명령일자
                Header(text = stringResource(id = R.string.recallCommandDate))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "2023-07-27 목",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF595959),
                        textAlign = TextAlign.Right,
                    ),
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // 회수 사유
            Header(text = stringResource(id = R.string.recallSaleSuspensionReason))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "최신 영국약전 개정내용 미반영된 제조번호에 대한 영업자회수",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 제조번호(사용기한)
            CardBox(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 54.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Header(text = stringResource(id = R.string.manufacturingNumber))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "22001[2025-01-16]",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 공개마감일자
            CardBox(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 54.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Header(text = stringResource(id = R.string.opendEndDate))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "2026-07-26 일",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
