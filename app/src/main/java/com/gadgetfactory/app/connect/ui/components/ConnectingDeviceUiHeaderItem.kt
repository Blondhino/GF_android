package com.gadgetfactory.app.connect.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.core.ui.components.BodyExtraSmallText
import com.gadgetfactory.app.core.ui.components.BodySmallText
import com.gadgetfactory.app.core.ui.components.Image
import com.gadgetfactory.app.core.ui.components.ImageType
import com.gadgetfactory.app.core.ui.components.ImageType.Resource

@Composable
fun ConnectingDeviceUiHeaderItem(
    deviceName: String,
    headerMessage: String,
    image: ImageType.Resource,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(25))
                    .size(40.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(25),
                    ),
                contentScale = ContentScale.Crop,
                imageType = image,
            )

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                BodySmallText(text = deviceName, fontWeight = SemiBold)
                BodyExtraSmallText(
                    text = headerMessage,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
