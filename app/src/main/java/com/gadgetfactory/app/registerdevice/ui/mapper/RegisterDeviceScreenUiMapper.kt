package com.gadgetfactory.app.registerdevice.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenState
import com.gadgetfactory.app.ui.global.snack.SnackbarPayload
import com.gadgetfactory.app.ui.global.snack.SnackbarType.ErrorSnackbar
import com.gadgetfactory.app.ui.global.snack.SnackbarType.WarningSnackbar

class RegisterDeviceScreenUiMapper(
    private val dictionary: Dictionary,
) {
    fun map(
        isScanning: Boolean = false,
    ) = RegisterDeviceScreenState(
        isScanning = isScanning,
        isButtonVisible = !isScanning,
        buttonText = dictionary.getString(R.string.register_device_screen_button_scan),
        screenMessage = dictionary.getString(R.string.register_device_screen_message),
        devices = emptyList(),
        bluetoothPermissionWarning = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_permission_warning_message),
            title = dictionary.getString(R.string.bluetooth_permission_warning_title),
            type = WarningSnackbar,
        ),
        bluetoothPermissionError = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_permission_error_message),
            title = dictionary.getString(R.string.bluetooth_permission_error_title),
            type = ErrorSnackbar,
            actionTitle = dictionary.getString(R.string.bluetooth_permission_error_action_text),
        ),

        bluetoothAdapterWarning = SnackbarPayload(
            message = dictionary.getString(R.string.bluetooth_adapter_error_message),
            title = dictionary.getString(R.string.bluetooth_adapter_error_title),
            type = WarningSnackbar,
        ),

    )
}
