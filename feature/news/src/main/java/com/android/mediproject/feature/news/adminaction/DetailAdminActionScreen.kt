package com.android.mediproject.feature.news.adminaction

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
import com.android.mediproject.core.model.remote.adminaction.AdminActionListItemDto
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun DetailAdminActionScreen(
    viewModel: AdminActionViewModel = hiltViewModel()
) {
    viewModel.getClickedItem()
    val item = viewModel.clickedItem.collectAsState()

    item.value?.apply {
        Item(item = this)
    }
}

@Composable
fun Item(item: AdminActionListItemDto) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = item.entpName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = item.itemName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = item.lastSettleDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = item.disposition, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 12.dp))
            Text(text = item.violation, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 12.dp))
            Text(text = item.applyLaw, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 12.dp))

        }
    }
}