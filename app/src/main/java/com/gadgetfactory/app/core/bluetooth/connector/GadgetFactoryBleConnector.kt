package com.gadgetfactory.app.core.bluetooth.connector

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.STATE_CONNECTED
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import com.gadgetfactory.app.core.bluetooth.connector.model.ConnectorError.CharacteristicsNotFound
import com.gadgetfactory.app.core.bluetooth.connector.model.ConnectorError.LiveDataStreamDisabled
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceInfo
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ConnectedToBackend
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ConnectedToWiFi
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Connecting
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ReachingBackend
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.SendingCredentials
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnectWiFi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

@SuppressLint("MissingPermission")
class GadgetFactoryBleConnector(private val context: Context) : BleConnector {
    private val bluetoothManager =
        context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter = bluetoothManager.adapter
    private var currentGatt: BluetoothGatt? = null
    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var wifiScanResultCharacteristic: BluetoothGattCharacteristic? = null
    private var ssidCharacteristic: BluetoothGattCharacteristic? = null
    private var passCharacteristic: BluetoothGattCharacteristic? = null
    private var apiKeyCharacteristic: BluetoothGattCharacteristic? = null
    private var wifiConnectionResultCharacteristic: BluetoothGattCharacteristic? = null
    private var macAddressResultCharacteristic: BluetoothGattCharacteristic? = null
    private var onWifiNetworkFoundCallback: (String) -> Unit = {}
    private var onWifiNetworkConnectionStateChanged: (state: String) -> Unit = {}
    private var onDeviceInfoFragmentReceived: (infoFragment: String) -> Unit = {}
    private var shouldKeepConnectionAlive: Boolean = true
    private var isConnectingProcessActive: Boolean = false

    override fun connectWithDevice(
        address: String,
    ): Flow<DeviceBleConnectionState> = callbackFlow {
        isConnectingProcessActive = true
        shouldKeepConnectionAlive = true

        trySend(DeviceBleConnectionState.Connecting)
        adapter?.let { bleAdapter ->
            bleAdapter.getRemoteDevice(address)?.connectGatt(
                context,
                false,
                object : BluetoothGattCallback() {

                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt,
                        status: Int,
                        newState: Int,
                    ) {
                        if (newState == STATE_CONNECTED) {
                            gatt.discoverServices()
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            if (shouldKeepConnectionAlive) {
                                connectWithDevice(address)
                            } else {
                                trySend(DeviceBleConnectionState.Disconnected)
                            }
                        }
                    }

                    override fun onServicesDiscovered(
                        gatt: BluetoothGatt,
                        status: Int,
                    ) {
                        currentGatt = gatt
                        val service = gatt.getService(WIFI_SERVICE_UUID)
                        commandCharacteristic =
                            service?.getCharacteristic(COMMAND_CHARACTERISTIC_UUID)
                        wifiScanResultCharacteristic =
                            service?.getCharacteristic(WIFI_SCAN_RESULT_CHARACTERISTIC_UUID)
                        ssidCharacteristic =
                            service?.getCharacteristic(SSID_CHARACTERISTIC_UUID)
                        passCharacteristic =
                            service?.getCharacteristic(PASS_CHARACTERISTIC_UUID)
                        apiKeyCharacteristic =
                            service?.getCharacteristic(API_KEY_CHARACTERISTIC_UUID)
                        wifiConnectionResultCharacteristic =
                            service?.getCharacteristic(WIFI_CONNECT_RESULT_CHARACTERISTIC_UUID)
                        macAddressResultCharacteristic =
                            service?.getCharacteristic(MAC_AND_TYPE_CHARACTERISTIC_UUID)
                        if (commandCharacteristic == null ||
                            wifiScanResultCharacteristic == null ||
                            ssidCharacteristic == null ||
                            passCharacteristic == null ||
                            wifiConnectionResultCharacteristic == null ||
                            macAddressResultCharacteristic == null ||
                            apiKeyCharacteristic == null
                        ) {
                            trySend(
                                DeviceBleConnectionState.UnableToConnect(CharacteristicsNotFound),
                            )
                            close()
                        }
                        gatt.setCharacteristicNotification(wifiScanResultCharacteristic, true)
                        gatt.setCharacteristicNotification(wifiConnectionResultCharacteristic, true)
                        gatt.setCharacteristicNotification(macAddressResultCharacteristic, true)
                        macAddressResultCharacteristic?.let {
                            val descriptor = it.getDescriptor(CCC_DESCRIPTOR_UUID)
                            descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            gatt.writeDescriptor(descriptor)
                        }
                        wifiScanResultCharacteristic?.let {
                            val descriptor = it.getDescriptor(CCC_DESCRIPTOR_UUID)
                            descriptor?.value =
                                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            gatt.writeDescriptor(descriptor)

                            trySend(DeviceBleConnectionState.Connected)
                        }
                    }

                    override fun onDescriptorWrite(
                        gatt: BluetoothGatt,
                        descriptor: BluetoothGattDescriptor,
                        status: Int,
                    ) {
                        if (status != BluetoothGatt.GATT_SUCCESS) {
                            trySend(
                                DeviceBleConnectionState.UnableToConnect(
                                    LiveDataStreamDisabled,
                                ),
                            )
                            close()
                        }
                    }

                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt,
                        characteristic: BluetoothGattCharacteristic,
                    ) {
                        if (characteristic.uuid == WIFI_SCAN_RESULT_CHARACTERISTIC_UUID) {
                            val wiFiNetwork = characteristic.value.toString(Charsets.UTF_8)
                            onWifiNetworkFoundCallback(wiFiNetwork)
                        }
                        if (characteristic.uuid == WIFI_CONNECT_RESULT_CHARACTERISTIC_UUID) {
                            val connectionResult = characteristic.value.toString(Charsets.UTF_8)
                            onWifiNetworkConnectionStateChanged(connectionResult)
                        }
                        if (characteristic.uuid == MAC_AND_TYPE_CHARACTERISTIC_UUID) {
                            val macAddress = characteristic.value.toString(Charsets.UTF_8)
                            onDeviceInfoFragmentReceived(macAddress)
                        }
                    }
                },
            )
        }

        awaitClose {
            isConnectingProcessActive = false
            shouldKeepConnectionAlive = false
        }
    }

    override fun scanWiFiNetworks(): Flow<List<String>> {
        var availableNetworks: MutableSet<String> = mutableSetOf()
        commandCharacteristic?.let {
            it.value = SCAN_WIFI_COMMAND.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(commandCharacteristic)
        }
        return callbackFlow {
            onWifiNetworkFoundCallback = { ssid ->
                if (isConnectingProcessActive) {
                    availableNetworks.add(ssid)
                    trySend(availableNetworks.toList())
                }
            }
            awaitClose {
                currentGatt?.setCharacteristicNotification(wifiScanResultCharacteristic, false)
                commandCharacteristic = null
                wifiScanResultCharacteristic = null
                currentGatt?.close()
                currentGatt = null
            }
        }
    }

    override fun getDeviceInfo(): Flow<DeviceInfo> = callbackFlow {
        var deviceInfo = DeviceInfo(
            mac = "",
            type = "",
        )
        onDeviceInfoFragmentReceived = {
            if (it.startsWith("type=")) {
                deviceInfo = deviceInfo.copy(type = it.removePrefix("type="))
                trySend(deviceInfo)
                close()
            } else {
                deviceInfo = deviceInfo.copy(mac = it)
            }
        }
        commandCharacteristic?.let {
            it.value = GET_MAC_ADDRESS_COMMAND.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(it)
        }

        awaitClose {}
    }

    override fun disconnectCurrentDevice() {
        isConnectingProcessActive = false
        shouldKeepConnectionAlive = false
        currentGatt?.disconnect()
    }

    override fun provideWiFiCredentialsAndConnect(
        ssid: String,
        password: String,
    ): Flow<DeviceWiFiConnectionState> = callbackFlow {
        delay(250)
        send(SendingCredentials)
        ssidCharacteristic?.let {
            it.value = ssid.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(ssidCharacteristic)
        }
        delay(250)
        passCharacteristic?.let {
            it.value = password.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(passCharacteristic)
        }
        send(Connecting)
        onWifiNetworkConnectionStateChanged = { state ->
            when (state) {
                WIFI_CONNECTION_SUCCESS -> {
                    trySend(ConnectedToWiFi)
                    trySend(ReachingBackend)
                }
                WIFI_CONNECTION_FAILED -> trySend(UnableToConnectWiFi)
                WS_CONNECTION_FAILED -> trySend(UnableToConnectWiFi)
                WS_CONNECTION_SUCCESS -> {
                    trySend(ConnectedToBackend)
                    currentGatt?.disconnect()
                    isConnectingProcessActive = false
                    shouldKeepConnectionAlive = false
                }
            }
        }
        awaitClose { }
    }

    override suspend fun provideUserIdAndApiKey(
        id: String,
        apiKey: String,
    ) {
        apiKeyCharacteristic?.let {
            val apiKeyForSending = "apiKey:" + apiKey + "userId:" + id
            it.value = apiKeyForSending.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(apiKeyCharacteristic)
        }
    }

    companion object {
        private val WIFI_SERVICE_UUID = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb")
        private val COMMAND_CHARACTERISTIC_UUID =
            UUID.fromString("00002aac-0000-1000-8000-00805f9b34fb")
        private val WIFI_SCAN_RESULT_CHARACTERISTIC_UUID =
            UUID.fromString("00002aad-0000-1000-8000-00805f9b34fb")
        private val WIFI_CONNECT_RESULT_CHARACTERISTIC_UUID =
            UUID.fromString("00002ab0-0000-1000-8000-00805f9b34fb")
        private val CCC_DESCRIPTOR_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        private val SSID_CHARACTERISTIC_UUID =
            UUID.fromString("00002aae-0000-1000-8000-00805f9b34fb")
        private val PASS_CHARACTERISTIC_UUID =
            UUID.fromString("00002aaf-0000-1000-8000-00805f9b34fb")
        private val MAC_AND_TYPE_CHARACTERISTIC_UUID =
            UUID.fromString("00002ab2-0000-1000-8000-00805f9b34fb")
        private val API_KEY_CHARACTERISTIC_UUID =
            UUID.fromString("00002ab1-0000-1000-8000-00805f9b34fb")

        private const val SCAN_WIFI_COMMAND = "start_scan_wifi"
        private const val WIFI_CONNECTION_SUCCESS = "wifi_connected"
        private const val WIFI_CONNECTION_FAILED = "wifi_failed"
        private const val WS_CONNECTION_SUCCESS = "ws_connected"
        private const val WS_CONNECTION_FAILED = "ws_failed"
        private const val GET_MAC_ADDRESS_COMMAND = "get_mac"
    }
}
