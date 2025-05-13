package com.gadgetfactory.app.scan.ui.interaction

sealed class ScanScreenViewEffect {
    data object CheckBluetoothPermission : ScanScreenViewEffect()
    data object OpenAppSettings : ScanScreenViewEffect()
    data object ShowBluetoothPermissionWarning : ScanScreenViewEffect()
    data object ShowBluetoothAdapterWarning : ScanScreenViewEffect()
    data object ShowBluetoothPermissionError : ScanScreenViewEffect()
}
