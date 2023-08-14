package com.android.mediproject.feature.news.safetynotification

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.mediproject.core.common.viewmodel.onInitial
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.model.news.safetynotification.SafetyNotification
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.R
import com.android.mediproject.feature.news.customui.CardBox
import com.android.mediproject.feature.news.customui.Header
import com.android.mediproject.feature.news.listDateTimeFormat


@Composable
fun DetailSafetyNotificationScreen(
    pop: () -> Unit,
) {
    BackHandler {
        pop()
    }

    val viewModel: SafetyNotificationViewModel = hiltViewModel()
    val item = viewModel.clickedItem.collectAsStateWithLifecycle()

    item.value.onInitial {
        CenterProgressIndicator(stringResource(id = R.string.loadingSafetyNotification))
    }.onSuccess {
        Item(it)
    }

}


@OptIn(ExperimentalTextApi::class)
@Composable
fun Item(safetyNotification: SafetyNotification) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            // 제목
            Header(
                text = stringResource(id = R.string.title),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = safetyNotification.title,
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
                    text = safetyNotification.publicationDate.value.format(listDateTimeFormat),
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
                text = safetyNotification.informationSummary,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF595959),
                    textAlign = TextAlign.Left,
                ),
            )

            // 상세정보
            Header(text = stringResource(id = R.string.informationDetails))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = safetyNotification.informationDetails,
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
                text = safetyNotification.actions,
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
                        text = "${safetyNotification.department} - ${safetyNotification.manager}",
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
                    ClickableText(
                        text = buildAnnotatedString {
                            pushStringAnnotation(
                                tag = safetyNotification.attachmentFile,
                                annotation = safetyNotification.attachmentFile,
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                ),
                            ) {
                                append(safetyNotification.attachmentFile)
                            }
                        },
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF3E3C3C),
                            textAlign = TextAlign.Right,
                        ),
                        onClick = {

                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
