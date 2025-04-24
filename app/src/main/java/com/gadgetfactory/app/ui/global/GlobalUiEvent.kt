package com.gadgetfactory.app.ui.global

import androidx.compose.runtime.Composable
import com.gadgetfactory.app.ui.components.BackgroundColorMode

sealed interface GlobalUiEvent {
    data class SetBackgroundColorMode(val colorMode: BackgroundColorMode) : GlobalUiEvent
    data class ShowHeader(val content: @Composable () -> Unit) : GlobalUiEvent
    data object HideHeader : GlobalUiEvent
}
