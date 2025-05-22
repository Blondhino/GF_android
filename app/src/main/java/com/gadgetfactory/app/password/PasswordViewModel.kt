package com.gadgetfactory.app.password

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.Disconnected
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceWiFiConnectionState.UnableToConnect
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent.PasswordChanged
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent.PasswordSubmit
import com.gadgetfactory.app.password.interaction.PasswordScreenState
import com.gadgetfactory.app.password.interaction.PasswordScreenViewEffect
import com.gadgetfactory.app.password.interaction.PasswordScreenViewEffect.ShowSnackbar
import com.gadgetfactory.app.password.mapper.InvalidPasswordSnackbarMapper
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
    private val snackbarErrorMapper: InvalidPasswordSnackbarMapper,
) : ScreenModel {

    private val password = MutableStateFlow("")
    private val connectingState: MutableStateFlow<DeviceWiFiConnectionState> =
        MutableStateFlow(Disconnected)
    private val _viewEffects = Channel<PasswordScreenViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    val uiState = combine(
        password,
        connectingState.onEach {
            if (it is UnableToConnect) {
                _viewEffects.send(ShowSnackbar(snackbarErrorMapper.map()))
            }
        },
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
                bleConnector.stopScanningWiFiNetworks()
                bleConnector.provideWiFiCredentialsAndConnect(
                    ssid = selectedWifiNetwork,
                    password = event.password,
                ).collect {
                    connectingState.value = it
                }
            }
        }
    }
}
