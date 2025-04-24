package com.gadgetfactory.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileInfoUiComponent(
    profileInfo: ProfileInfoUiComponentData,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            Modifier
                .size(26.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                .clip(CircleShape)
                .background(color = Color.Transparent),
        )
        BodyMediumText(
            text = profileInfo.profileTitle,
        )
    }
}

data class ProfileInfoUiComponentData(
    val profileImageUrl: String,
    val profileTitle: String,
)
