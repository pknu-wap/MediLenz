package com.android.mediproject.feature.news

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.mediproject.core.model.navargs.RecallDisposalArgs
import com.android.mediproject.feature.news.adminaction.AdminActionScreen
import com.android.mediproject.feature.news.recallsalesuspension.RecallDisposalScreen
import com.android.mediproject.feature.news.safetynotification.SafetyNotificationScreen
import kotlinx.parcelize.Parcelize

/**
 * 뉴스 타입
 */
@Parcelize
sealed class ChipType(@StringRes val nameStringId: Int) : Parcelable {
    object RecallSaleSuspension : ChipType(R.string.recallSaleSuspension)

    object SafetyNotification : ChipType(R.string.safetyNotification)

    object AdminAction : ChipType(R.string.adminAction)
}

/**
 * 뉴스 화면
 */
@Composable
fun NewsNavHost(
    arguments: RecallDisposalArgs,
) {
    val navController: NavHostController = rememberNavController()

    val startDestination by remember {
        mutableStateOf(
            if (arguments.product.isNotEmpty()) RecallSaleSuspensionRoutes.RecallSaleSuspensionRoutesDetail.uri
            else MainRoutes.News.uri,
        )
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(MainRoutes.News.uri) {
            NewsScreen()
        }
    }
}

@Composable
fun NewsScreen() {
    val navController = rememberNavController()
    var selectedChip: ChipType by rememberSaveable { mutableStateOf(ChipType.RecallSaleSuspension) }

    Column {
        ChipGroup(
            selectedChip,
            onChipSelected = { chip ->
                selectedChip = chip
            },
        )
        Divider(modifier = Modifier.padding(horizontal = 24.dp))
        when (selectedChip) {
            is ChipType.RecallSaleSuspension -> RecallDisposalScreen()
            is ChipType.AdminAction -> AdminActionScreen()
            is ChipType.SafetyNotification -> SafetyNotificationScreen()
        }
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
    val spacerModifier = Modifier.width(8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 24.dp, end = 24.dp),
    ) {
        CustomFilterChip(
            title = stringResource(id = ChipType.RecallSaleSuspension.nameStringId),
            isSelected = selectedChip == ChipType.RecallSaleSuspension,
            type = ChipType.RecallSaleSuspension,
        ) {
            onChipSelected(ChipType.RecallSaleSuspension)
        }
        Spacer(spacerModifier)
        CustomFilterChip(
            title = stringResource(id = ChipType.AdminAction.nameStringId),
            isSelected = selectedChip == ChipType.AdminAction,
            type = ChipType.AdminAction,
        ) {
            onChipSelected(ChipType.AdminAction)
        }
        Spacer(spacerModifier)
        CustomFilterChip(
            title = stringResource(id = ChipType.SafetyNotification.nameStringId),
            isSelected = selectedChip == ChipType.SafetyNotification,
            type = ChipType.SafetyNotification,
        ) {
            onChipSelected(ChipType.RecallSaleSuspension)
        }
    }
}

/**
 * 뉴스 타입 Chip
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFilterChip(
    type: ChipType, title: String, isSelected: Boolean, onClick: (ChipType) -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = { onClick.invoke(type) },
        label = { Text(title, fontSize = 13.sp) },
        shape = RoundedCornerShape(36.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Blue,
            selectedLabelColor = Color.White,
            disabledContainerColor = Color.White,
            disabledLabelColor = Color.Blue,
        ),
    )
}
