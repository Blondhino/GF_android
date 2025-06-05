package com.gadgetfactory.app.core.bluetooth.connector.model

sealed interface DeviceBleConnectionState {
    data object Connecting : DeviceBleConnectionState
    data object Connected : DeviceBleConnectionState
    data object Disconnected : DeviceBleConnectionState
    data class UnableToConnect(val error: ConnectorError) : DeviceBleConnectionState
}
