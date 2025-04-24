package com.gadgetfactory.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.ui.theme.LightIndigo
import com.gadgetfactory.app.ui.theme.SilverMist

@Composable
fun TextPill(
    text: String,
    id: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit = {},
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) LightIndigo else MaterialTheme.colorScheme.onSurface,
        label = "borderColor",
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else SilverMist,
        label = "backgroundColor",
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50),
            )
            .background(backgroundColor)
            .clickable(onClick = { onClick(id) }),
    ) {
        BodyExtraSmallText(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
    }
}
