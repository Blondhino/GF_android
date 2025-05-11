package com.gadgetfactory.app.registerdevice.ui.interaction

import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget
import com.gadgetfactory.app.ui.global.snack.SnackbarPayload

data class RegisterDeviceScreenState(
    val isScanning: Boolean,
    val isButtonVisible: Boolean,
    val buttonText: String,
    val devices: List<FoundGadget>,
    val shouldShowDevicesList: Boolean,
    val scanAgainButtonText: String,
    val scanAgainButtonVisible: Boolean,
    val bluetoothPermissionError: SnackbarPayload,
    val bluetoothPermissionWarning: SnackbarPayload,
    val bluetoothAdapterWarning: SnackbarPayload,
)
