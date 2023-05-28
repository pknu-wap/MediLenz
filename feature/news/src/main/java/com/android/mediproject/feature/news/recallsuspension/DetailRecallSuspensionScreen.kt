package com.android.mediproject.feature.news.recallsuspension

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionItemDto
import com.android.mediproject.core.ui.compose.CenterProgressIndicator
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun DetailRecallDisposalScreen(
    viewModel: DetailRecallSuspensionViewModel = hiltViewModel(), navController: NavController = rememberNavController()
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

        is UiState.Initial -> {}
    }

}

@Composable
fun Item(item: DetailRecallSuspensionItemDto) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = item.product, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = item.enterprise, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = item.reasonForRecall, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 12.dp))
            Text(
                text = item.recallCommandDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(text = item.manufactureNo, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 12.dp))
        }
    }
}