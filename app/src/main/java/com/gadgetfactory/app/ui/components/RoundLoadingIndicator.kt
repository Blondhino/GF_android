package com.gadgetfactory.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun RoundLoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(60.dp),
        strokeWidth = 5.dp,
        color = MaterialTheme.colorScheme.secondary,
        strokeCap = StrokeCap.Round,
    )
}
