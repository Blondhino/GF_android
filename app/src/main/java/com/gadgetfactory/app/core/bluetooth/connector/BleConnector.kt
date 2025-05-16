package com.gadgetfactory.app.core.bluetooth.connector

import kotlinx.coroutines.flow.Flow

interface BleConnector {
    fun connectWithDevice(
        address: String,
        onError: (ConnectorError) -> Unit,
        onConnected: () -> Unit,
    )

    fun scanWiFiNetworks(): Flow<String>
    fun stopScanningWiFiNetworks()
    fun disconnectCurrentDevice()
}

sealed interface ConnectorEvent {
    data class NewWiFiNetworkFound(val ssid: String) : ConnectorEvent
    data class WiFiConnectingStateChange(val state: DeviceWiFiConnectionState) : ConnectorEvent
    data class ServerConnectingStateChange(val state: DeviceServerConnectionState) :
        ConnectorEvent
}

sealed interface DeviceBleConnectionState {
    data object Connecting : DeviceBleConnectionState
    data object Connected : DeviceBleConnectionState
    data object Disconnected : DeviceBleConnectionState
    data class UnableToConnect(val message: String) : DeviceBleConnectionState
}

sealed interface DeviceWiFiConnectionState {
    data object Connected : DeviceWiFiConnectionState
    data object UnableToConnect : DeviceWiFiConnectionState
}

sealed interface DeviceServerConnectionState {
    data object Connected : DeviceServerConnectionState
    data object UnableToConnect : DeviceServerConnectionState
}

sealed interface ConnectorError {
    data object BleAdapterNotFound : ConnectorError
    data object UnableToConnectWithBleDevice : ConnectorError
    data object LiveDataStreamDisabled : ConnectorError
    data object CharacteristicsNotFound : ConnectorError
}
