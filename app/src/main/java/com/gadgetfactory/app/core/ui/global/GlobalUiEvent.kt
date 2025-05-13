package com.gadgetfactory.app.core.ui.global

import androidx.compose.runtime.Composable
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode

sealed interface GlobalUiEvent {
    data class SetBackgroundColorMode(val colorMode: BackgroundColorMode) : GlobalUiEvent
    data class ShowHeader(val content: @Composable () -> Unit) : GlobalUiEvent
    data object HideHeader : GlobalUiEvent
}
