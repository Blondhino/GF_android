package com.gadgetfactory.app.dashboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.core.ui.theme.SilverMist
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption

@Composable
fun IconRoundedUiComponent(
    data: HeaderOption,
    onClick: (HeaderOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(SilverMist)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            .clickable { onClick(data) },
    ) {
        androidx.compose.foundation.Image(
            painterResource(data.icon),
            contentDescription = data.toString(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
                .size(12.dp),

        )
    }
}
