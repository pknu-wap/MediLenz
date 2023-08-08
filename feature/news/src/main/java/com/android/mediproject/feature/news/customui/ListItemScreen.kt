package com.android.mediproject.feature.news.customui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape


@Composable
internal fun ListItemScreen(onClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        onClick = {
            onClick()
        },
        content = content,
    )
}
