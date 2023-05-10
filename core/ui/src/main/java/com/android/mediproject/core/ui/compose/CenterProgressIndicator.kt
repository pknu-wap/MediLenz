package com.android.mediproject.core.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenterProgressIndicator(showOfItem: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = if (showOfItem) Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp) else Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}