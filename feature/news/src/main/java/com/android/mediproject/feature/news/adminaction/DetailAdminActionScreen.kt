package com.android.mediproject.feature.news.adminaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.CardBox
import com.android.mediproject.feature.news.customui.Header

@Composable
fun DetailAdminActionScreen(
    viewModel: AdminActionViewModel = hiltViewModel(),
) {
    viewModel.getClickedItem()
    val item = viewModel.clickedItem.collectAsState()

    item.value?.apply {
        //Item(item = this)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Item() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
       ,
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // 업체명
            Header(
                text = stringResource(id = R.string.companyName),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "동우당제약(주)",
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                ),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                // 업체 공장주소
                Header(text = stringResource(id = R.string.manufactureAddress))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "경상북도 의성군 봉양면 농공신동길 27",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF32649F),
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 행정처분일자(최종확정일자)
                Header(text = stringResource(id = R.string.adminActionDate))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "2023-08-01 월",
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

            // 제품명(품목기준코드)
            Header(text = stringResource(id = R.string.productNameWithItemSeq))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "동우당건강(200406266)",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 위반내용
            Header(text = stringResource(id = R.string.violationDetails))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "한약재 수거 검사 결과 '부적합' 판정",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 행정처분명
            Header(text = stringResource(id = R.string.adminAction))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "  ￮ ‘동우당건강(제118호)’ 품목 제조업무 정지 3개월 (2023. 8. 16. ~ 2023. 11. 15.)",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 위반법령
            Header(text = stringResource(id = R.string.violationLaw))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = """
                      ￮ 위반법령: 「약사법」제62조
                      ￮ 처분법령: 「약사법」제76조 및 「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] Ⅱ. 개별기준 제39호라목11)
                """.trimIndent(),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF3E3C3C),
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 사업자등록번호
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Header(text = stringResource(id = R.string.bizrNo))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "5028163816",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 업체일련번호
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Header(text = stringResource(id = R.string.companyRegistrationNumber))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "20020096",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),

                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 공개마감일자
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Header(text = stringResource(id = R.string.disclosureEndDate))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "2024-02-14 수",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),

                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 행정처분일련번호
            CardBox(
                modifier = Modifier.align(Alignment.End),
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Header(text = stringResource(id = R.string.adminActionNumber))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "2023005985",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF595959),
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
