package com.gadgetfactory.app.password.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenHeaderState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Connecting
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Disconnected
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ReachingBackend
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnectWiFi
import com.gadgetfactory.app.core.devices.mapGFDeviceImage
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenState

class PasswordScreenUiMapper(
    private val dictionary: Dictionary,
) {
    fun map(
        password: String,
        connectionState: DeviceWiFiConnectionState,
        deviceName: String,
    ) = PasswordScreenState.Content(
        screenMessage = dictionary.getString(R.string.password_screen_message),
        password = password,
        hint = dictionary.getString(R.string.password_screen_input_field_hint),
        isTextFieldEnabled = connectionState is Disconnected || connectionState is UnableToConnectWiFi,
        headerState = ConnectScreenHeaderState(
            deviceImage = mapGFDeviceImage(deviceName),
            isLoading = connectionState is Connecting || connectionState is ReachingBackend,
            deviceName = deviceName,
            message = when (connectionState) {
                is Connecting -> dictionary.getString(R.string.connecting_device_to_the_network)
                is ReachingBackend -> dictionary.getString(R.string.reaching_backend_message)
                else -> dictionary.getString(R.string.provide_wifi_credentials)
            },
        ),
    )
}
