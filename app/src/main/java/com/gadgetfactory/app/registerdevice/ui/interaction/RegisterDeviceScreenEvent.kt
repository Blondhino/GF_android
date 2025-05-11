package com.gadgetfactory.app.registerdevice.ui.interaction

import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions

sealed interface RegisterDeviceScreenEvent {
    data object StartScanClick : RegisterDeviceScreenEvent
    data object ScanAgainClick : RegisterDeviceScreenEvent
    data object PermissionErrorDismissed : RegisterDeviceScreenEvent
    data object AdapterWarningDismissed : RegisterDeviceScreenEvent
    data object OpenAppSettingsClicked : RegisterDeviceScreenEvent
    data class OnCheckPermissionsResult(val results: BluetoothPermissions) :
        RegisterDeviceScreenEvent
}
