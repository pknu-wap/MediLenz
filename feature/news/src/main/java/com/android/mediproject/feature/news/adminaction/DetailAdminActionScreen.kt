package com.android.mediproject.feature.news.adminaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.mediproject.core.common.viewmodel.onInitial
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.model.news.adminaction.AdminAction
import com.android.mediproject.feature.news.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.CardBox
import com.android.mediproject.feature.news.customui.Header
import com.android.mediproject.feature.news.listDateTimeFormat

@Composable
fun DetailAdminActionScreen(
    pop: () -> Unit,
) {
    BackHandler {
        pop()
    }

    val viewModel: AdminActionViewModel = hiltViewModel()
    val item by viewModel.clickedItem.collectAsStateWithLifecycle()

    item.onInitial {
        CenterProgressIndicator(stringResource(id = R.string.loadingAdminActionList))
    }.onSuccess {
        Item(it)
    }
}

@Composable
fun Item(adminAction: AdminAction) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            // 업체명
            Header(
                text = stringResource(id = R.string.companyName),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = adminAction.companyName,
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
                    text = adminAction.companyAddress,
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
                    text = adminAction.adminActionDate.value.format(listDateTimeFormat),
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
                text = "${adminAction.itemName}(${adminAction.itemSeq})",
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
                text = adminAction.violationDetails,
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
                text = adminAction.adminAction,
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
                text = adminAction.violationLaw,
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
                        text = adminAction.companyBizrNo,
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
                        text = adminAction.companyRegistrationNumber,
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
                        text = adminAction.disclosureEndDate.value.format(listDateTimeFormat),
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
                        text = adminAction.adminActionNo,
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
