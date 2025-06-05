package com.gadgetfactory.app.core.ui.theme

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.gadgetfactory.app.core.ui.components.ApplicationContainer

private val DarkColorScheme = darkColorScheme(
    surface = Onyx,
    onSurface = Steel,
    tertiary = Frost,
    background = Ebony,
    secondary = Indigo,
    error = Carmine,
    scrim = Amber,
    surfaceVariant = Lime,
    onBackground = Silver,

)

@Composable
fun GadgetFactoryTheme(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
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
            ApplicationContainer(
                modifier = modifier,
                content = content,
                paddingValues = paddingValues,
            )
        },
    )
}
