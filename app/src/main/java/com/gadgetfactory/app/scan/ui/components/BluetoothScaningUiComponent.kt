package com.gadgetfactory.app.scan.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.gadgetfactory.app.core.ui.theme.LightIndigo

@Composable
fun BluetoothScanningUiComponent(
    isScanning: Boolean,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.circlescan))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = IterateForever,
        restartOnPlay = true,
    )
    val animationAlpha by animateFloatAsState(if (isScanning) 1f else 0f, tween(1000))
    val imageAlpha by animateFloatAsState(if (isScanning) 1f else 0.5f)
    Column(modifier = modifier.animateContentSize()) {
        Box {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .alpha(animationAlpha),
                composition = composition,
                progress = { progress },
            )
            Image(
                modifier = Modifier
                    .padding(32.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = 1.dp,
                        color = LightIndigo.copy(alpha = 0.5f),
                        shape = CircleShape,
                    )
                    .alpha(imageAlpha)
                    .padding(16.dp),

                painter = painterResource(R.drawable.ic_bluetooth),
                contentDescription = null,
            )
        }
    }
}
