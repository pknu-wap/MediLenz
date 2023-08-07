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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.mediproject.core.model.adminaction.AdminAction
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun DetailAdminActionScreen(
    viewModel: AdminActionViewModel = hiltViewModel(),
) {
    viewModel.getClickedItem()
    val item = viewModel.clickedItem.collectAsState()

    item.value?.apply {
        Item(item = this)
    }
}

@Composable
fun Item(item: AdminAction) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 25.dp),
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = item.companyName,
                fontSize = TextUnit(20f, type = TextUnitType.Sp),
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = item.itemName,
                fontSize = TextUnit(20f, type = TextUnitType.Sp),
                style = TextStyle(color = "#32649F".color),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.Right,
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = item.adminActionDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                fontSize = TextUnit(14f, type = TextUnitType.Sp),
                style = TextStyle(color = "#595959".color),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.Right,
            )

            Divider(Modifier.padding(top = 20.dp, bottom = 40.dp))

            Text(
                text = item.violationDetails,
                fontSize = TextUnit(14f, type = TextUnitType.Sp),
                modifier = Modifier.padding(bottom = 8.dp),
                color = "#3E3C3C".color,
            )

            Text(
                text = item.adminAction,
                fontSize = TextUnit(14f, type = TextUnitType.Sp),
                modifier = Modifier.padding(bottom = 8.dp),
                color = "#3E3C3C".color,
            )

            Text(
                text = item.violationLaw,
                fontSize = TextUnit(14f, type = TextUnitType.Sp),
                modifier = Modifier.padding(bottom = 8.dp),
                color = "#3E3C3C".color,
            )
        }
    }
}


val String.color
    get() = Color(android.graphics.Color.parseColor(this))
