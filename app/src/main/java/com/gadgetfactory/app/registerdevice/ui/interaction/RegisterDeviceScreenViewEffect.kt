package com.gadgetfactory.app.registerdevice.ui.interaction

sealed class RegisterDeviceScreenViewEffect {
    data object CheckBluetoothPermission : RegisterDeviceScreenViewEffect()
    data object OpenAppSettings : RegisterDeviceScreenViewEffect()
    data object ShowBluetoothPermissionWarning : RegisterDeviceScreenViewEffect()
    data object ShowBluetoothAdapterWarning : RegisterDeviceScreenViewEffect()
    data object ShowBluetoothPermissionError : RegisterDeviceScreenViewEffect()
}
