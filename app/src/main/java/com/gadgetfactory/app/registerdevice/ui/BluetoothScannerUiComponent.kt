package com.gadgetfactory.app.registerdevice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gadgetfactory.app.R
import com.gadgetfactory.app.ui.components.BodySmallText
import com.gadgetfactory.app.ui.components.PrimaryButton
import com.gadgetfactory.app.ui.theme.LightIndigo

@Composable
fun BluetoothScannerUiComponent(
    isActive: Boolean,
    message: String,
    buttonText: String,
    onButtonClick: (isActive: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.circlescan))
    val animationAlpha by animateFloatAsState(targetValue = if (isActive) 1f else 0f)
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = IterateForever,
        isPlaying = isActive,
        restartOnPlay = true,
    )

    Box {
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(animationAlpha),
            composition = composition,
            progress = { progress },
        )

        Image(
            modifier = Modifier
                .clip(
                    CircleShape,
                )
                .align(Alignment.Center)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.background)
                .border(width = 1.dp, color = LightIndigo.copy(alpha = 0.5f), shape = CircleShape)
                .padding(16.dp),

            painter = painterResource(R.drawable.ic_bluetooth),
            contentDescription = message,
        )
    }
    BodySmallText(message, modifier = Modifier.padding(top = 16.dp))
    AnimatedVisibility(
        !isActive,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        PrimaryButton(
            onClick = { onButtonClick(!isActive) },
            text = buttonText,
        )
    }
}
