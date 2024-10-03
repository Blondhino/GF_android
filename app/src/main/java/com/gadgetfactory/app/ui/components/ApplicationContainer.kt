package com.gadgetfactory.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiEvent.SetBackgroundColorMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

@Composable
fun ApplicationContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val globalUi: GlobalUi = koinInject()
    var firstColor by rememberSaveable { mutableIntStateOf(Normal.colors.first().toArgb()) }
    var secondColor by rememberSaveable { mutableIntStateOf(Normal.colors.last().toArgb()) }

    LaunchedEffect(Unit) {
        globalUi.globalUiEvent.onEach {
            when (it) {
                is SetBackgroundColorMode -> {
                    firstColor = it.colorMode.colors.first().toArgb()
                    secondColor = it.colorMode.colors.last().toArgb()
                }
            }
        }.launchIn(this)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        AnimatedGradientContainer(
            content = content,
            firstColor = firstColor,
            secondColor = secondColor,
        )
    }
}
