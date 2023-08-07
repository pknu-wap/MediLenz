package com.android.mediproject.feature.news.customui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
internal fun CardBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(size = 16.dp),
        color = Color(0xFFFFFFFF),
    ) {
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp), contentAlignment = Alignment.Center) {
            content()
        }
    }
}
