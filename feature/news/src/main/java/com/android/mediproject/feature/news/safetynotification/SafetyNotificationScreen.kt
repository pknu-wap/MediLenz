package com.android.mediproject.feature.news.safetynotification

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun SafetyNotificationScreen() {

}


@Preview(showBackground = true)
@Composable
fun ListItem() {
    Surface(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {

        },
    ) {
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
                text = "제목",
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier
                    .constrainAs(title) {
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
                text = "정보",
                textAlign = TextAlign.Left,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(title.bottom, 11.dp)
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
                text = "2023-07-21 금",
                textAlign = TextAlign.Right,
                modifier = Modifier.constrainAs(date) {
                    start.linkTo(title.end)
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
