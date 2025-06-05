package com.gadgetfactory.app.details.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.core.ui.components.BodyLargeText
import com.gadgetfactory.app.core.ui.components.BodySmallText
import com.gadgetfactory.app.core.ui.components.Image
import com.gadgetfactory.app.core.ui.theme.SilverMist
import com.gadgetfactory.app.details.domain.model.DeviceInfographic

@Composable
fun DetailsScreenInfographicItem(
    infographic: DeviceInfographic,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(140.dp)
            .clip(RoundedCornerShape(25))
            .border(1.dp, colorScheme.onSurface, RoundedCornerShape(25))
            .background(SilverMist),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(imageType = infographic.icon, modifier = Modifier.size(18.dp))
            BodyLargeText(
                text = infographic.value,
                fontWeight = SemiBold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            BodySmallText(text = infographic.description, color = colorScheme.onBackground)
        }
    }
}
