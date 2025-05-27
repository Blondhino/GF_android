package com.gadgetfactory.app.core.bluetooth.connector.model

sealed interface DeviceWiFiConnectionState {
    data object SendingCredentials : DeviceWiFiConnectionState
    data object Connecting : DeviceWiFiConnectionState
    data object ReachingBackend : DeviceWiFiConnectionState
    data object ConnectedToWiFi : DeviceWiFiConnectionState
    data object ConnectedToBackend : DeviceWiFiConnectionState
    data object UnableToConnectWiFi : DeviceWiFiConnectionState
    data object UnableToConnectBackend : DeviceWiFiConnectionState
    data object Disconnected : DeviceWiFiConnectionState
}
