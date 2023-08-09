package com.android.mediproject.feature.news.recallsuspension

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.recall.DetailRecallSuspension
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import com.android.mediproject.feature.news.adminaction.color
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun DetailRecallDisposalScreen(
    viewModel: DetailRecallSuspensionViewModel = hiltViewModel(), navController: NavController = rememberNavController(),
) {

    val uiState = viewModel.detailRecallSuspension.collectAsState()

    when (val state = uiState.value) {
        is UiState.Loading -> {
            CenterProgressIndicator(true)
        }

        is UiState.Success -> {
            Item(item = state.data)
        }

        is UiState.Error -> {

        }

        is UiState.Init -> {}
    }

}

// 회수 폐기
@Composable
fun Item(item: DetailRecallSuspension) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 25.dp),
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = item.product,
                    fontSize = TextUnit(16f, type = TextUnitType.Sp),
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    text = item.enterprise,
                    modifier = Modifier.align(Alignment.End),
                    fontSize = TextUnit(16f, type = TextUnitType.Sp),
                    style = TextStyle(color = "#32649F".color),
                    textAlign = TextAlign.Right,
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = item.recallCommandDate.format(DateTimeFormatter.ISO_DATE),
                    fontSize = TextUnit(14f, type = TextUnitType.Sp),
                    style = TextStyle(color = "#595959".color),
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.Right,
                )

                Divider(Modifier.padding(top = 20.dp, bottom = 40.dp))

                Text(
                    text = item.reasonForRecall,
                    fontSize = TextUnit(14f, type = TextUnitType.Sp),
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = "#3E3C3C".color,
                )
            }
        }
    }
}
