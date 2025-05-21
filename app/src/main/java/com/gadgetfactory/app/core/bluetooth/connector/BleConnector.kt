package com.gadgetfactory.app.core.bluetooth.connector

import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import kotlinx.coroutines.flow.Flow

interface BleConnector {
    fun connectWithDevice(address: String): Flow<DeviceBleConnectionState>
    fun scanWiFiNetworks(): Flow<List<String>>
    fun stopScanningWiFiNetworks()
    fun disconnectCurrentDevice()
    fun provideWiFiCredentialsAndConnect(
        ssid: String,
        password: String,
    ): Flow<DeviceWiFiConnectionState>
}
