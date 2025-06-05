package com.gadgetfactory.app.password.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ConnectedToBackend
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.ConnectedToWiFi
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Disconnected
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnectBackend
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnectWiFi
import com.gadgetfactory.app.password.domain.RegisterCurrentlyConnectedDevice
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenEvent
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenEvent.PasswordChanged
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenEvent.PasswordSubmit
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenState
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenViewEffect
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenViewEffect.GoToDashboard
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenViewEffect.SetErrorBackground
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenViewEffect.SetSuccessBackground
import com.gadgetfactory.app.password.ui.interaction.PasswordScreenViewEffect.ShowSnackbar
import com.gadgetfactory.app.password.ui.mapper.ConnectedDeviceSnackbarMapper
import com.gadgetfactory.app.password.ui.mapper.InvalidPasswordSnackbarMapper
import com.gadgetfactory.app.password.ui.mapper.PasswordScreenUiMapper
import com.gadgetfactory.app.password.ui.mapper.UnableConnectBackendSnackbarMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val bleConnector: BleConnector,
    private val selectedWifiNetwork: String,
    private val deviceName: String,
    private val uiMapper: PasswordScreenUiMapper,
    private val invalidPassSnackbar: InvalidPasswordSnackbarMapper,
    private val unableConnectBackendSnackbar: UnableConnectBackendSnackbarMapper,
    private val connectedDeviceSnackbar: ConnectedDeviceSnackbarMapper,
    private val registerCurrentlyConnectedDevice: RegisterCurrentlyConnectedDevice,
) : ScreenModel {

    private val password = MutableStateFlow("")
    private val wifiConnectionState: MutableStateFlow<DeviceWiFiConnectionState> =
        MutableStateFlow(Disconnected)
    private val _viewEffects = Channel<PasswordScreenViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    val uiState = combine(
        password,
        wifiConnectionState.onEach { it.handleWiFiConnectionState() },
        flowOf(deviceName),
        uiMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PasswordScreenState.Loading,
    )

    fun onEvent(event: PasswordScreenEvent) {
        when (event) {
            is PasswordChanged -> password.update { event.password }
            is PasswordSubmit -> screenModelScope.launch {
                bleConnector.provideWiFiCredentialsAndConnect(
                    ssid = selectedWifiNetwork,
                    password = event.password,
                ).collect {
                    wifiConnectionState.value = it
                }
            }
        }
    }

    private suspend fun DeviceWiFiConnectionState.handleWiFiConnectionState() = when (this) {
        is ConnectedToWiFi -> registerCurrentlyConnectedDevice()
        is UnableToConnectWiFi -> {
            _viewEffects.send(SetErrorBackground)
            _viewEffects.send(ShowSnackbar(invalidPassSnackbar.map()))
        }

        is UnableToConnectBackend -> {
            _viewEffects.send(SetErrorBackground)
            _viewEffects.send(ShowSnackbar(unableConnectBackendSnackbar.map()))
        }

        is ConnectedToBackend -> {
            _viewEffects.send(SetSuccessBackground)
            _viewEffects.send(ShowSnackbar(connectedDeviceSnackbar.map()))
            _viewEffects.send(GoToDashboard)
        }
        else -> Unit // no-op
    }
}
