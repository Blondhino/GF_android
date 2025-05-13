package com.gadgetfactory.app.scan.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.ui.global.snack.SnackbarPayload
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType.ErrorSnackbar
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType.WarningSnackbar
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenState

class ScanScreenUiMapper(
    private val dictionary: Dictionary,
) {
    fun map(
        isScanning: Boolean = false,
        devices: List<FoundGadget> = emptyList(),
    ) = ScanScreenState(
        isScanning = isScanning,
        isButtonVisible = !isScanning && devices.isEmpty(),
        buttonText = dictionary.getString(R.string.register_device_screen_button_scan),
        devices = devices.filter { it.name.isNotEmpty() },
        shouldShowDevicesList = devices.isNotEmpty(),
        scanAgainButtonText = dictionary.getString(R.string.register_device_screen_button_scan_again),
        scanAgainButtonVisible = devices.isNotEmpty() && !isScanning,

        bluetoothPermissionError = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_permission_error_message),
            title = dictionary.getString(R.string.bluetooth_permission_error_title),
            type = ErrorSnackbar,
            actionTitle = dictionary.getString(R.string.bluetooth_permission_error_action_text),
        ),
        bluetoothPermissionWarning = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_permission_warning_message),
            title = dictionary.getString(R.string.bluetooth_permission_warning_title),
            type = WarningSnackbar,
        ),
        bluetoothAdapterWarning = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_adapter_error_message),
            title = dictionary.getString(R.string.bluetooth_adapter_error_title),
            type = WarningSnackbar,
        ),
    )
}
