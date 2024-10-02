package com.gadgetfactory.app.ui.theme

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.random.Random

private val DarkColorScheme = darkColorScheme(
    surface = Onyx,
    onSurface = Steel,
    tertiary = Frost,
    background = Ebony,
)

@Composable
fun GadgetFactoryTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val activity = LocalContext.current as ComponentActivity

    SideEffect {
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = {
            Surface(
                modifier = modifier,
                color = MaterialTheme.colorScheme.background,
            ) {
                AnimatedGradientContainer(content = content)
            }
        },
    )
}

@Composable
fun AnimatedGradientContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var screenWidth by remember { mutableIntStateOf(0) }
    var screenHeight by remember { mutableIntStateOf(0) }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val speedPerSecond = 50f // constant speed in pixels per second

    LaunchedEffect(Unit) {
        if (screenWidth != 0 && screenHeight != 0) {
            offsetX.snapTo((screenWidth / 2).toFloat())
            offsetY.snapTo((screenHeight / 2).toFloat())
            while (true) {
                val randomX = Random.nextInt(-50, screenWidth + 50).toFloat()
                val randomY = Random.nextInt(-50, screenHeight / 2).toFloat()
                val distance = hypot(randomX - offsetX.value, randomY - offsetY.value)
                val durationMillis = (distance / speedPerSecond * 1000).toInt()

                launch {
                    offsetX.animateTo(
                        randomX,
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = LinearEasing,
                        ),
                    )
                }
                launch {
                    offsetY.animateTo(
                        randomY,
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = LinearEasing,
                        ),
                    )
                }

                delay(durationMillis.toLong())
            }
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                // Capture the screen dimensions
                screenWidth = layoutCoordinates.size.width
                screenHeight = layoutCoordinates.size.height
            }
            .drawBehind {
                // Draw the moving gradient with the current animated offsets
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x99181449), Color(0x00080808)),
                        center = Offset(offsetX.value, offsetY.value),
                        radius = 400.dp.toPx(),
                    ),
                )
            },
    ) {
        content()
    }
}
