package com.gadgetfactory.app.core.bluetooth.connector.model

sealed interface DeviceServerConnectionState {
    data object Connected : DeviceServerConnectionState
    data object UnableToConnect : DeviceServerConnectionState
}
