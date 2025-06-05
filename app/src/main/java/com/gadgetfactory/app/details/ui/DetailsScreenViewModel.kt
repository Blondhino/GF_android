package com.gadgetfactory.app.details.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.details.domain.ConnectToDeviceSocketUseCase
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenEvent
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenEvent.DeviceSettingsClicked
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState
import com.gadgetfactory.app.details.ui.mapper.DetailsScreenHeaderUiMapper
import com.gadgetfactory.app.details.ui.mapper.DetailsScreenUiMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsScreenViewModel(
    private val userId: String,
    private val deviceName: String,
    private val connectToDevice: ConnectToDeviceSocketUseCase,
    private val headerMapper: DetailsScreenHeaderUiMapper,
    private val uiMapper: DetailsScreenUiMapper,
) : ScreenModel {
    private val isHeaderLoading = MutableStateFlow(true)
    val headerUiState = isHeaderLoading.map {
        headerMapper.map(isLoading = it, deviceName = deviceName)
    }.stateIn(
        scope = screenModelScope,
        started = WhileSubscribed(5_000),
        initialValue = headerMapper.map(isLoading = true, deviceName = deviceName),
    )

    private val measurements = connectToDevice(userId).onEach {
        if (it.isRight()) isHeaderLoading.update { false }
    }

    val uiState = measurements
        .mapLatest {
            uiMapper.map(it)
        }
        .stateIn(
            scope = screenModelScope,
            started = WhileSubscribed(5_000),
            initialValue = DetailsScreenState.Loading,
        )

    fun onEvent(event: DetailsScreenEvent) {
        when (event) {
            is DeviceSettingsClicked -> {}
        }
    }
}
