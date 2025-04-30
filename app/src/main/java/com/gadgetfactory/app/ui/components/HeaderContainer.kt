package com.gadgetfactory.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.ui.theme.SilverMist

@Composable
fun HeaderContainer(
    content: @Composable () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    onOffsetChanged: (offsetFromTheScreenTop: Dp) -> Unit,
) {
    val density = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(SilverMist)
            .padding(paddingValues)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                val bottomOffsetPx = bounds.bottom - statusBarHeightPx
                val bottomOffsetDp = with(density) { bottomOffsetPx.toDp() }
                val safeOffset = bottomOffsetDp.coerceAtLeast(0.dp)
                onOffsetChanged(safeOffset)
            },
    ) { content() }
}
