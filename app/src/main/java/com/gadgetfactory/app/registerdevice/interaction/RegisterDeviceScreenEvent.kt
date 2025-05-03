package com.gadgetfactory.app.registerdevice.interaction

sealed interface RegisterDeviceScreenEvent {
    data object ScreenShown : RegisterDeviceScreenEvent
}
