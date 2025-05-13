package com.gadgetfactory.app.core.ui.global.snack

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.ui.components.BodyLargeText
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.components.Image
import com.gadgetfactory.app.core.ui.components.ImageType
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType.ErrorSnackbar
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType.SuccessSnackbar
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType.WarningSnackbar
import com.gadgetfactory.app.core.ui.theme.Amber
import com.gadgetfactory.app.core.ui.theme.Carmine
import com.gadgetfactory.app.core.ui.theme.Frost
import com.gadgetfactory.app.core.ui.theme.Lime
import com.gadgetfactory.app.core.ui.theme.Onyx
import com.gadgetfactory.app.core.ui.theme.Steel

@Composable
fun SnackUiMessage(
    payload: SnackbarPayload,
    duration: SnackbarDuration,
    modifier: Modifier = Modifier,
    cancelable: Boolean = true,
    onCancel: () -> Unit = {},
    onAction: () -> Unit = {},
) {
    val handleColor = when (payload.type) {
        ErrorSnackbar -> Carmine
        SuccessSnackbar -> Lime
        WarningSnackbar -> Amber
    }

    val handleSize = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        if (duration != SnackbarDuration.Indefinite) {
            handleSize.animateTo(
                0f,
                animationSpec = tween(
                    durationMillis = when {
                        duration == SnackbarDuration.Short -> 4000
                        else -> 10000
                    },
                    easing = LinearEasing,
                ),
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        if (cancelable) CloseImage(onClose = onCancel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Onyx)
                .border(
                    shape = RoundedCornerShape(18.dp),
                    width = 1.dp,
                    color = Steel,
                )
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight(handleSize.value)
                    .clip(RoundedCornerShape(50))
                    .background(handleColor),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.padding(vertical = 5.dp),
            ) {
                BodyLargeText(text = payload.title, color = Frost, fontWeight = SemiBold)
                BodyMediumText(text = payload.message, color = Frost, fontWeight = Normal)
                payload.actionTitle?.let {
                    ActionText(
                        onAction = onAction,
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CloseImage(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClose)
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .size(24.dp),
        imageType = ImageType.Resource(R.drawable.ic_close),
    )
}

@Composable
fun ActionText(
    onAction: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        BodyMediumText(
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .clickable { onAction() }
                .align(Alignment.CenterEnd)
                .padding(8.dp),
            textAlign = TextAlign.End,
            text = text,
            color = Frost,
            fontWeight = SemiBold,
        )
    }
}
