package com.gadgetfactory.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.gadgetfactory.app.ui.components.ImageType.Remote
import com.gadgetfactory.app.ui.components.ImageType.Resource

@Composable
fun Image(
    imageType: ImageType,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    when (imageType) {
        is Remote -> AsyncImage(
            model = imageType.url,
            modifier = modifier,
            contentDescription = null,
            contentScale = contentScale,
        )

        is Resource -> Image(
            painter = painterResource(imageType.resource),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

sealed interface ImageType {
    data class Remote(val url: String) : ImageType
    data class Resource(@DrawableRes val resource: Int) : ImageType
}
