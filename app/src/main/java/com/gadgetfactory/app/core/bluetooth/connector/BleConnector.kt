package com.gadgetfactory.app.core.bluetooth.connector

import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceInfo
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import kotlinx.coroutines.flow.Flow

interface BleConnector {
    fun connectWithDevice(address: String): Flow<DeviceBleConnectionState>
    fun scanWiFiNetworks(): Flow<List<String>>
    fun getDeviceInfo(): Flow<DeviceInfo>
    fun disconnectCurrentDevice()
    fun provideWiFiCredentialsAndConnect(
        ssid: String,
        password: String,
    ): Flow<DeviceWiFiConnectionState>
    suspend fun provideUserIdAndApiKey(
        id: String,
        apiKey: String,
    )
}
