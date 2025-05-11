package com.gadgetfactory.app.registerdevice.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions
import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions.AdapterTurnedOff
import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions.AllGranted
import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions.AnyPermanentlyDenied
import com.gadgetfactory.app.core.bluetooth.BluetoothPermissions.SomeDenied
import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScanner
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.AdapterWarningDismissed
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.OnCheckPermissionsResult
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.OpenAppSettingsClicked
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.PermissionErrorDismissed
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.ScanAgainClick
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.StartScanClick
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.CheckBluetoothPermission
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.OpenAppSettings
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothAdapterWarning
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothPermissionError
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothPermissionWarning
import com.gadgetfactory.app.registerdevice.ui.mapper.RegisterDeviceScreenUiMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class RegisterDeviceViewModel(
    private val uiMapper: RegisterDeviceScreenUiMapper,
    private val gadgetScanner: GadgetScanner,
) : ScreenModel {

    private val _uiState = MutableStateFlow(uiMapper.map())
    val uiState = _uiState.asStateFlow()
    private val _viewEffect = Channel<RegisterDeviceScreenViewEffect>(Channel.BUFFERED)
    val viewEffect = _viewEffect.receiveAsFlow()

    fun onEvent(event: RegisterDeviceScreenEvent) {
        when (event) {
            is StartScanClick -> screenModelScope.launch { _viewEffect.send(CheckBluetoothPermission) }
            is OnCheckPermissionsResult -> event.results.handlePermissionsResults()
            is PermissionErrorDismissed -> _uiState.update { it.copy(isButtonVisible = true) }
            is OpenAppSettingsClicked -> handleAppSettingsClicked()
            is AdapterWarningDismissed -> _uiState.update { it.copy(isButtonVisible = true) }
            is ScanAgainClick -> {
                _uiState.update { it.copy(scanAgainButtonVisible = false) }
                startWithScanning()
            }
        }
    }

    private fun handleAppSettingsClicked() = screenModelScope.launch {
        _viewEffect.send(OpenAppSettings)
        _uiState.update { it.copy(isButtonVisible = true) }
    }

    private fun BluetoothPermissions.handlePermissionsResults() = screenModelScope.launch {
        when (this@handlePermissionsResults) {
            is AllGranted -> startWithScanning()
            is AnyPermanentlyDenied -> handlePermissionDenied()
            is SomeDenied -> {
                _uiState.update { it.copy(isButtonVisible = false) }
                _viewEffect.send(ShowBluetoothPermissionWarning)
            }

            is AdapterTurnedOff -> {
                _uiState.update { it.copy(isButtonVisible = false) }
                _viewEffect.send(ShowBluetoothAdapterWarning)
            }
        }
    }

    private fun startWithScanning() = gadgetScanner.discoverGadgets(
        scanDuration = 15.seconds,
        onScanStarted = { _uiState.update { uiMapper.map(isScanning = true) } },
        onScanStopped = {
            _uiState.update { uiMapper.map(isScanning = false, devices = it.devices) }
        },
    )
        .onLeft { _uiState.update { uiMapper.map(isScanning = false) } }
        .onRight { it.handleFoundDevices() }

    private fun handlePermissionDenied() = screenModelScope.launch {
        _uiState.update { it.copy(isButtonVisible = false) }
        _viewEffect.send(ShowBluetoothPermissionError)
    }

    private fun Flow<List<FoundGadget>>.handleFoundDevices() = screenModelScope.launch {
        this@handleFoundDevices.collect { devices ->
            _uiState.update { uiMapper.map(devices = devices, isScanning = true) }
        }
    }
}
