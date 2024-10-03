package com.gadgetfactory.app.ui.global

import com.gadgetfactory.app.ui.components.BackgroundColorMode

sealed interface GlobalUiEvent {
    data class SetBackgroundColorMode(val colorMode: BackgroundColorMode) : GlobalUiEvent
}
