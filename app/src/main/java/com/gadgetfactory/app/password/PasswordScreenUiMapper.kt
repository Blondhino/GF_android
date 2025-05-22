package com.gadgetfactory.app.password

import com.gadgetfactory.app.R
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenHeaderState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Connecting
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Disconnected
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnect
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.utils.mapGFDeviceImage
import com.gadgetfactory.app.password.interaction.PasswordScreenState

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
        isTextFieldEnabled = connectionState is Disconnected || connectionState is UnableToConnect,
        headerState = ConnectScreenHeaderState(
            deviceImage = mapGFDeviceImage(deviceName),
            isLoading = connectionState is Connecting,
            deviceName = deviceName,
            message = if (connectionState is Connecting) {
                dictionary.getString(R.string.connecting_device_to_the_network)
            } else {
                dictionary.getString(R.string.provide_wifi_credentials)
            },
        ),
    )
}
