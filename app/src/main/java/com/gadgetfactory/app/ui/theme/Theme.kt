package com.gadgetfactory.app.ui.theme

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
            ) { content() }
        },
    )
}
