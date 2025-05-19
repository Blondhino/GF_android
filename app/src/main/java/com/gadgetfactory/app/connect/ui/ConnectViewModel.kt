package com.gadgetfactory.app.connect.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.connect.data.ConnectScreenUiMapper
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent.WiFiNetworkSelected
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Loading
import com.gadgetfactory.app.connect.ui.interaction.ConnectViewEffect
import com.gadgetfactory.app.connect.ui.interaction.ConnectViewEffect.HideHeader
import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.core.bluetooth.connector.DeviceBleConnectionState.Connected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectViewModel(
    private val bleConnector: BleConnector,
    private val deviceName: String,
    private val deviceAddress: String,
    private val uiMapper: ConnectScreenUiMapper,
) : ScreenModel {
    private val _viewEffects = Channel<ConnectViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()
    private val bleConnectionState = bleConnector.connectWithDevice(deviceAddress)
    private val availableNetworks = bleConnectionState.flatMapLatest { state ->
        when (state) {
            is Connected -> bleConnector.scanWiFiNetworks()
            else -> MutableStateFlow(emptyList())
        }
    }

    val uiState = combine(
        bleConnectionState,
        availableNetworks,
        flowOf(deviceName),
        uiMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading,
    )

    fun onEvent(event: ConnectScreenEvent) {
        when (event) {
            is WiFiNetworkSelected -> {}
        }
    }

    override fun onDispose() {
        _viewEffects.trySend(HideHeader)
        super.onDispose()
    }
}
