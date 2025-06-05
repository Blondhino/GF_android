package com.gadgetfactory.app.details.ui.interaction

sealed interface DetailsScreenEvent {
    data object DeviceSettingsClicked : DetailsScreenEvent
}
