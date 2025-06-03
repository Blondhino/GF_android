package com.gadgetfactory.app.dashboard.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.auth.domain.usecase.CurrentUser
import com.gadgetfactory.app.auth.domain.usecase.Logout
import com.gadgetfactory.app.dashboard.data.mapper.DashboardHeaderMapper
import com.gadgetfactory.app.dashboard.data.mapper.DashboardUiMapper
import com.gadgetfactory.app.dashboard.data.mapper.DevicesStateMapper
import com.gadgetfactory.app.dashboard.domain.GetDashboardHeader
import com.gadgetfactory.app.dashboard.domain.GetMyDevices
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.LogoutClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnDeviceSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.RoomSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.HideHeader
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToDetails
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToRegisterDeviceScreen
import com.gadgetfactory.app.dashboard.ui.model.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.ui.model.DashboardScreenState
import com.gadgetfactory.app.dashboard.ui.model.DeviceUiItem
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption.AddDevice
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption.EditRooms
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption.Notifications
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    getHeader: GetDashboardHeader,
    private val uiMapper: DashboardUiMapper,
    private val headerMapper: DashboardHeaderMapper,
    private val logout: Logout,
    private val getMyDevices: GetMyDevices,
    private val devicesStateMapper: DevicesStateMapper,
    private val getCurrentUser: CurrentUser,
) : ScreenModel {

    private val _devicesFetchTrigger = Channel<Unit>(Channel.BUFFERED)
    private val _selectedRoom = MutableStateFlow("")
    private val _viewEffect = Channel<DashboardViewEffect>(Channel.BUFFERED)
    val viewEffect = _viewEffect.receiveAsFlow()
    private val _devices =
        _devicesFetchTrigger
            .receiveAsFlow()
            .onStart { emit(Unit) }
            .map { devicesStateMapper.map(getMyDevices()) }

    private val _header = combine(
        getHeader(),
        _selectedRoom,
        headerMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardHeaderUiState.Loading,
    )

    val uiState = combine(
        _header,
        _devices,
        uiMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardScreenState.Loading,
    )

    fun onEvent(event: DashboardScreenEvent) {
        when (event) {
            is LogoutClicked -> {}
            is RoomSelected -> _selectedRoom.update { event.roomId }
            is OnHeaderOptionClicked -> handleOptionClicked(event.option)
            is OnDeviceSelected -> openDetails(event.device)
        }
    }

    private fun openDetails(device: DeviceUiItem) = screenModelScope.launch {
        getCurrentUser().map { user ->
            _viewEffect.send(NavigateToDetails(deviceId = device.id, userId = user.id))
        }
    }

    private fun handleOptionClicked(option: HeaderOption) = screenModelScope.launch {
        when (option) {
            is AddDevice -> {
                _viewEffect.send(HideHeader)
                _viewEffect.send(NavigateToRegisterDeviceScreen)
            }

            is EditRooms -> {
                logout()
                _viewEffect.send(HideHeader)
                _viewEffect.send(NavigateToAuthScreen)
            }

            is Notifications -> {}
        }
    }
}
