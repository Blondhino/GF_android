package com.gadgetfactory.app.registerdevice.ui.interaction

import com.gadgetfactory.app.ui.global.snack.SnackbarPayload

data class RegisterDeviceScreenState(
    val isScanning: Boolean,
    val isButtonVisible: Boolean,
    val buttonText: String,
    val screenMessage: String,
    val devices: List<String>,
    val bluetoothPermissionError: SnackbarPayload,
    val bluetoothPermissionWarning: SnackbarPayload,
    val bluetoothAdapterWarning: SnackbarPayload,
)
