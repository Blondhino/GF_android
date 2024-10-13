package com.gadgetfactory.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.random.Random

@Composable
fun AnimatedGradientContainer(
    firstColor: Int,
    secondColor: Int,
    paddingValues: PaddingValues,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var screenWidth by rememberSaveable { mutableIntStateOf(0) }
    var screenHeight by remember { mutableIntStateOf(0) }
    var offsetX by rememberSaveable { mutableFloatStateOf((screenWidth / 2).toFloat()) }
    var offsetY by rememberSaveable { mutableFloatStateOf((screenHeight / 2).toFloat()) }
    val speedPerSecond = 50f
    var randomX by rememberSaveable { mutableFloatStateOf(0f) }
    var randomY by rememberSaveable { mutableFloatStateOf(0f) }
    var durationMillis by rememberSaveable { mutableLongStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            offsetX = ((screenWidth / 2).toFloat())
            offsetY = ((screenHeight / 2).toFloat())
            randomX = Random.nextInt(-50, screenWidth + 50).toFloat()
            randomY = Random.nextInt(-50, screenHeight / 2).toFloat()
            val distance = hypot(randomX - offsetX, randomY - offsetY)
            durationMillis = (distance / speedPerSecond * 1000).toLong()
            delay(durationMillis)
        }
    }

    val firstGradientColor = animateColorAsState(
        targetValue = Color(firstColor),
        animationSpec = tween(durationMillis = 1000),
        label = "firstColor",
    ).value

    val secondGradientColor = animateColorAsState(
        targetValue = Color(secondColor),
        animationSpec = tween(durationMillis = 1000),
        label = "secondColor",
    ).value

    offsetX = animateFloatAsState(
        targetValue = randomX,
        animationSpec = tween(durationMillis = durationMillis.toInt(), easing = LinearEasing),
        label = "offsetX",
    ).value

    offsetY = animateFloatAsState(
        targetValue = randomY,
        animationSpec = tween(durationMillis = durationMillis.toInt(), easing = LinearEasing),
        label = "offsetY",
    ).value

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                screenWidth = layoutCoordinates.size.width
                screenHeight = layoutCoordinates.size.height
            }
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(firstGradientColor, secondGradientColor),
                        center = Offset(offsetX, offsetY),
                        radius = 400.dp.toPx(),
                    ),
                )
            }
            .padding(paddingValues),
    ) {
        content()
    }
}

sealed class BackgroundColorMode(val colors: List<Color>) {
    data object Normal : BackgroundColorMode(listOf(Color(0x99181449), Color(0x00080808)))
    data object Error : BackgroundColorMode(listOf(Color(0x99491414), Color(0x00080808)))
    data object Warning : BackgroundColorMode(listOf(Color(0x99B07213), Color(0x00080808)))
    data class Custom(val customColors: List<Color>) : BackgroundColorMode(customColors)
}
