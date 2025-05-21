package com.gadgetfactory.app.core.bluetooth.connector.model

sealed interface ConnectorError {
    data object BleAdapterNotFound : ConnectorError
    data object UnableToConnectWithBleDevice : ConnectorError
    data object LiveDataStreamDisabled : ConnectorError
    data object CharacteristicsNotFound : ConnectorError
}
