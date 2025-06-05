package com.gadgetfactory.app.connect.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.connect.data.ConnectScreenUiMapper
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent.WiFiNetworkSelected
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Loading
import com.gadgetfactory.app.connect.ui.interaction.ConnectViewEffect
import com.gadgetfactory.app.connect.ui.interaction.ConnectViewEffect.OpenPasswordScreen
import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceBleConnectionState.Connected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectViewModel(
    deviceName: String,
    deviceAddress: String,
    private val bleConnector: BleConnector,
    private val uiMapper: ConnectScreenUiMapper,
) : ScreenModel {
    private val _viewEffects = Channel<ConnectViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()
    private val bleConnectionState = bleConnector.connectWithDevice(deviceAddress)
    private val scannedNetworks: StateFlow<List<String>> = bleConnector
        .connectWithDevice(deviceAddress)
        .flatMapLatest { state ->
            if (state is Connected) {
                bleConnector.scanWiFiNetworks()
            } else {
                flowOf(emptyList())
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    val uiState = combine(
        bleConnectionState,
        scannedNetworks,
        flowOf(deviceName),
        uiMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading,
    )

    fun onEvent(event: ConnectScreenEvent) {
        when (event) {
            is WiFiNetworkSelected -> screenModelScope.launch {
                _viewEffects.send(OpenPasswordScreen(selectedWiFi = event.ssid))
            }
        }
    }
}
