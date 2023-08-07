package com.android.mediproject.feature.news.safetynotification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.CardBox
import com.android.mediproject.feature.news.customui.Header


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Item() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // 제목
            Header(
                text = stringResource(id = R.string.title),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "한국휴텍스제약㈜ 제조 ‘레큐틴정(트리메부틴말레산염)’등 6개 품목 잠정 제조·판매·사용 중지",
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                ),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                // 공개일자
                Header(text = stringResource(id = R.string.publicationDate))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "2023-07-21 금",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF32649F),
                    ),
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))


            // 정보요약
            Header(text = stringResource(id = R.string.informationSummary))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "❍ 한국휴텍스제약㈜에서 제조한 의약품 6개 품목에 대하여 잠정 제조·판매중지 명령 및 사용 중단을 요청함.",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF595959),
                    textAlign = TextAlign.Right,
                ),
            )

            // 상세정보
            Header(text = stringResource(id = R.string.informationDetails))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "❍ 식품의약품안전처는 한국휴텍스제약㈜에 대한 현장조사 결과, ‘레큐틴정’ 등 6개 품목이 허가 또는 신고된 사항과 다르게 제조되고 있는 사실이 확인됨에 따라 사전 예방적 차원에서 잠정 제조·판매 중지를 명령하고 해당 품목에 대하여 회수 조치함. ❍ 의․약전문가는 동 정보사항에 유의하여 해당 제품의 처방 및 사용을 중지하고 필요시 대체의약품을 사용하여 주실 것을 당부드리며, ❍ 아울러 해당 유통품 회수가 적절히 수행될 수 있도록 적극 협조하여 주시기 바람",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 조치 사항
            Header(text = stringResource(id = R.string.actions))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "❍ 한국휴텍스제약㈜에서 제조한 ‘레큐틴정’ 등 6개 품목(붙임)",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 담당부서, 담당자
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Header(text = stringResource(id = R.string.departmentAndContactPerson))
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "의약품관리과 : 정다현",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF3E3C3C),
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 첨부파일
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Header(text = stringResource(id = R.string.attachmentFile))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "https://nedrug.mfds.go.kr/cmn/edms/down/1ObfzbuFq4l",
                        textAlign = TextAlign.Right,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF3E3C3C),
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
