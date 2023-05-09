package com.android.mediproject.feature.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.mediproject.feature.news.recallsuspension.RecallDisposalScreen

enum class ChipType {
    RECALLS_SUSPENSION, ADMIN_ACTION
}

@Composable
fun NewsScreen() {
    var selectedChip by remember { mutableStateOf(ChipType.RECALLS_SUSPENSION) }

    Column {
        Text(
            text = stringResource(id = com.android.mediproject.core.ui.R.string.news),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        ChipGroup(selectedChip, onChipSelected = { chip ->
            selectedChip = chip
        })
        if (selectedChip == ChipType.RECALLS_SUSPENSION) RecallDisposalScreen()
        else Text(text = "AdminAction")

    }
}

@Composable
fun ChipGroup(selectedChip: ChipType, onChipSelected: (ChipType) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        CustomFilterChip(
            title = stringResource(id = R.string.recallSuspension),
            isSelected = selectedChip == ChipType.RECALLS_SUSPENSION,
            type = ChipType.RECALLS_SUSPENSION
        ) {
            onChipSelected(if (selectedChip == ChipType.RECALLS_SUSPENSION) ChipType.RECALLS_SUSPENSION else ChipType.ADMIN_ACTION)
        }
        Spacer(Modifier.width(8.dp))
        CustomFilterChip(
            title = stringResource(id = R.string.adminAction),
            isSelected = selectedChip == ChipType.ADMIN_ACTION,
            type = ChipType.ADMIN_ACTION
        ) {
            onChipSelected(if (selectedChip == ChipType.RECALLS_SUSPENSION) ChipType.RECALLS_SUSPENSION else ChipType.ADMIN_ACTION)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFilterChip(type: ChipType, title: String, isSelected: Boolean, onClick: (ChipType) -> Unit) {
    FilterChip(selected = isSelected, onClick = { onClick.invoke(type) }, label = { Text(title) })
}