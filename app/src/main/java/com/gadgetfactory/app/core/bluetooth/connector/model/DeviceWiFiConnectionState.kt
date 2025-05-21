package com.gadgetfactory.app.core.bluetooth.connector.model

sealed interface DeviceWiFiConnectionState {
    data object SendingCredentials : DeviceWiFiConnectionState
    data object Connecting : DeviceWiFiConnectionState
    data object Connected : DeviceWiFiConnectionState
    data object UnableToConnect : DeviceWiFiConnectionState
}
