package com.gadgetfactory.app.scan.ui.interaction

import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions

sealed interface ScanScreenEvent {
    data object StartScanClick : ScanScreenEvent
    data object ScanAgainClick : ScanScreenEvent
    data object PermissionErrorDismissed : ScanScreenEvent
    data object AdapterWarningDismissed : ScanScreenEvent
    data object OpenAppSettingsClicked : ScanScreenEvent
    data class OnCheckPermissionsResult(val results: BluetoothPermissions) :
        ScanScreenEvent
}
