package com.gadgetfactory.app.registerdevice.interaction

sealed class RegisterDeviceScreenViewEffect {
    data object CheckBluetoothPermission : RegisterDeviceScreenViewEffect()
}
