package com.gadgetfactory.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiEvent.HideHeader
import com.gadgetfactory.app.ui.global.GlobalUiEvent.SetBackgroundColorMode
import com.gadgetfactory.app.ui.global.GlobalUiEvent.ShowHeader
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

@Composable
fun ApplicationContainer(
    paddingValues: PaddingValues,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val globalUi: GlobalUi = koinInject()
    var firstColor by rememberSaveable { mutableIntStateOf(Normal.colors.first().toArgb()) }
    var secondColor by rememberSaveable { mutableIntStateOf(Normal.colors.last().toArgb()) }
    val isContainerVisible = rememberSaveable { mutableStateOf(false) }
    val headerContent: MutableState<@Composable () -> Unit> = remember { mutableStateOf({}) }
    var headerOffset by remember { mutableStateOf(0.dp) }
    LaunchedEffect(Unit) {
        globalUi.globalUiEvent.onEach {
            when (it) {
                is SetBackgroundColorMode -> {
                    firstColor = it.colorMode.colors.first().toArgb()
                    secondColor = it.colorMode.colors.last().toArgb()
                }

                is ShowHeader -> {
                    headerContent.value = it.content
                    isContainerVisible.value = true
                }

                is HideHeader -> isContainerVisible.value = false
            }
        }.launchIn(this)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            AnimatedGradientContainer(
                content = content,
                firstColor = firstColor,
                secondColor = secondColor,
                paddingValues = paddingValues,
                offsetFromTheTop = headerOffset,
            )
            AnimatedVisibility(
                visible = isContainerVisible.value,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            ) {
                HeaderContainer(
                    content = headerContent.value,
                    paddingValues = paddingValues,
                    onOffsetChanged = { headerOffset = it },
                )
            }
        }
    }
}
