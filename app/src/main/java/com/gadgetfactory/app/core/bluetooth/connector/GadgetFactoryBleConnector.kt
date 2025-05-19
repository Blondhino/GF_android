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
import android.util.Log
import com.gadgetfactory.app.core.bluetooth.connector.ConnectorError.CharacteristicsNotFound
import com.gadgetfactory.app.core.bluetooth.connector.ConnectorError.LiveDataStreamDisabled
import kotlinx.coroutines.channels.awaitClose
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
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null
    private var onWifiNetworkFoundCallback: (String) -> Unit = {}
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
                                Log.d("BLE_RESULT", "🔌 Disconnected --> automatic reconnect")
                                connectWithDevice(address)
                            } else {
                                Log.d("BLE_RESULT", "🔌 Disconnected")
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
                        notifyCharacteristic =
                            service?.getCharacteristic(NOTIFY_CHARACTERISTIC_UUID)

                        if (commandCharacteristic == null || notifyCharacteristic == null) {
                            trySend(
                                DeviceBleConnectionState.UnableToConnect(CharacteristicsNotFound),
                            )
                            close()
                        }
                        gatt.setCharacteristicNotification(notifyCharacteristic, true)
                        notifyCharacteristic?.let {
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
                        if (characteristic.uuid == NOTIFY_CHARACTERISTIC_UUID) {
                            val wiFiNetwork = characteristic.value.toString(Charsets.UTF_8)
                            onWifiNetworkFoundCallback(wiFiNetwork)
                        }
                    }
                },
            )
        }

        awaitClose {
            isConnectingProcessActive = false
            shouldKeepConnectionAlive = false
            currentGatt?.close()
            currentGatt = null
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
                currentGatt?.setCharacteristicNotification(notifyCharacteristic, false)
                commandCharacteristic = null
                notifyCharacteristic = null
                currentGatt?.close()
                currentGatt = null
            }
        }
    }

    override fun stopScanningWiFiNetworks() {
        commandCharacteristic?.let {
            it.value = STOP_SCAN_WIFI_COMMAND.toByteArray(Charsets.UTF_8)
            currentGatt?.writeCharacteristic(commandCharacteristic)
        }
    }

    override fun disconnectCurrentDevice() {
        Log.d("BLE_RESULT", "🔌 Disconnect called")
        isConnectingProcessActive = false
        shouldKeepConnectionAlive = false
        currentGatt?.disconnect()
    }

    companion object {
        private val WIFI_SERVICE_UUID = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb")
        private val COMMAND_CHARACTERISTIC_UUID =
            UUID.fromString("00002aac-0000-1000-8000-00805f9b34fb")
        private val NOTIFY_CHARACTERISTIC_UUID =
            UUID.fromString("00002aad-0000-1000-8000-00805f9b34fb")
        private val CCC_DESCRIPTOR_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

        private const val SCAN_WIFI_COMMAND = "start_scan_wifi"
        private const val STOP_SCAN_WIFI_COMMAND = "stop_scan_wifi"
    }
}
