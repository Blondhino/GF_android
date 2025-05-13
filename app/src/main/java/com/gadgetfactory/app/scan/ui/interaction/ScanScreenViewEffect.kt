package com.gadgetfactory.app.scan.ui.interaction

import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget

sealed class ScanScreenViewEffect {
    data object CheckBluetoothPermission : ScanScreenViewEffect()
    data object OpenAppSettings : ScanScreenViewEffect()
    data object ShowBluetoothPermissionWarning : ScanScreenViewEffect()
    data object ShowBluetoothAdapterWarning : ScanScreenViewEffect()
    data object ShowBluetoothPermissionError : ScanScreenViewEffect()
    data class GoToConnectPage(val gadget: FoundGadget) : ScanScreenViewEffect()
}
