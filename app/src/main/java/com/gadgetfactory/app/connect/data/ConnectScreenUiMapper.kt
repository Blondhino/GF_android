package com.gadgetfactory.app.connect.data

import com.gadgetfactory.app.R
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenHeaderState
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Content
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Error
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState.Connected
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState.Connecting
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState.UnableToConnect
import com.gadgetfactory.app.core.devices.mapGFDeviceImage
import com.gadgetfactory.app.core.dictionary.Dictionary

class ConnectScreenUiMapper(
    val dictionary: Dictionary,
) {

    fun map(
        bleConnectingState: DeviceBleConnectionState,
        availableNetworks: List<String>,
        deviceName: String,
    ): ConnectScreenState {
        val headerState: ConnectScreenHeaderState = mapHeaderState(
            bleConnectingState = bleConnectingState,
            deviceName = deviceName,
            availableNetworks,
        )
        return when (bleConnectingState) {
            is UnableToConnect -> Error
            else -> Content(
                headerState = headerState,
                availableNetworks = availableNetworks,
                screenMessage = if (availableNetworks.isNotEmpty()) "Select a WiFi network" else "",
            )
        }
    }

    private fun mapHeaderState(
        bleConnectingState: DeviceBleConnectionState,
        deviceName: String,
        availableNetworks: List<String>,
    ) = ConnectScreenHeaderState(
        deviceImage = mapGFDeviceImage(deviceName),
        isLoading = bleConnectingState == Connecting ||
            (bleConnectingState == Connected && availableNetworks.isEmpty()),
        deviceName = deviceName,
        message = if (availableNetworks.isEmpty()) {
            dictionary.getString(R.string.gathering_device_info)
        } else {
            dictionary.getString(R.string.provide_wifi_credentials)
        },
    )
}
