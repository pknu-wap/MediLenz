package com.android.mediproject.feature.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mediproject.feature.news.adminaction.AdminActionScreen
import com.android.mediproject.feature.news.recallsuspension.RecallDisposalScreen

/**
 * 뉴스 타입
 */
enum class ChipType {
    RECALLS_SUSPENSION, ADMIN_ACTION
}

/**
 * 뉴스 화면
 */
@Preview
@Composable
fun NewsScreen() {
    var selectedChip by remember { mutableStateOf(ChipType.RECALLS_SUSPENSION) }

    Column {
        ChipGroup(selectedChip, onChipSelected = { chip ->
            selectedChip = chip
        })
        if (selectedChip == ChipType.RECALLS_SUSPENSION) RecallDisposalScreen()
        else AdminActionScreen()
    }
}

/**
 * 뉴스 타입 선택
 *
 * @param selectedChip 선택된 뉴스 타입
 * @param onChipSelected 뉴스 타입 선택 시 호출되는 콜백
 */
@Composable
fun ChipGroup(selectedChip: ChipType, onChipSelected: (ChipType) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
    ) {
        CustomFilterChip(
            title = stringResource(id = R.string.recallSuspension),
            isSelected = selectedChip == ChipType.RECALLS_SUSPENSION,
            type = ChipType.RECALLS_SUSPENSION
        ) {
            onChipSelected(ChipType.RECALLS_SUSPENSION)
        }
        Spacer(Modifier.width(8.dp))
        CustomFilterChip(
            title = stringResource(id = R.string.adminAction),
            isSelected = selectedChip == ChipType.ADMIN_ACTION,
            type = ChipType.ADMIN_ACTION
        ) {
            onChipSelected(ChipType.ADMIN_ACTION)
        }
    }
}

/**
 * 뉴스 타입 Chip
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFilterChip(type: ChipType, title: String, isSelected: Boolean, onClick: (ChipType) -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = { onClick.invoke(type) },
        label = { Text(title, fontSize = 13.sp) },
        shape = RoundedCornerShape(36.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Blue,
            selectedLabelColor = Color.White,
            disabledContainerColor = Color.White,
            disabledLabelColor = Color.Blue
        ),
    )
}