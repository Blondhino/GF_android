package com.gadgetfactory.app.dashboard.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.auth.domain.usecase.Logout
import com.gadgetfactory.app.core.utils.OnetimeWhileSubscribed
import com.gadgetfactory.app.dashboard.data.mapper.DashboardHeaderMapper
import com.gadgetfactory.app.dashboard.data.mapper.DashboardUiMapper
import com.gadgetfactory.app.dashboard.domain.GetDashboardHeader
import com.gadgetfactory.app.dashboard.model.ui.DashboardScreenState
import com.gadgetfactory.app.dashboard.model.ui.HeaderOption
import com.gadgetfactory.app.dashboard.model.ui.HeaderOption.AddDevice
import com.gadgetfactory.app.dashboard.model.ui.HeaderOption.EditRooms
import com.gadgetfactory.app.dashboard.model.ui.HeaderOption.Notifications
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.LogoutClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.RoomSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.HideHeader
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToRegisterDeviceScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    getHeader: GetDashboardHeader,
    private val uiMapper: DashboardUiMapper,
    private val headerMapper: DashboardHeaderMapper,
    private val logout: Logout,
) : ScreenModel {

    private val _devices = MutableStateFlow(emptyList<Int>())
    private val _selectedRoom = MutableStateFlow("")
    private val _viewEffect = Channel<DashboardViewEffect>(Channel.BUFFERED)
    val viewEffect = _viewEffect.receiveAsFlow()

    private val _header = combine(
        getHeader(),
        _selectedRoom,
        headerMapper::map,
    )

    val uiState = combine(
        _header,
        _devices,
        uiMapper::map,
    ).stateIn(
        scope = screenModelScope,
        started = OnetimeWhileSubscribed(5_000L),
        initialValue = DashboardScreenState.Loading,
    )

    fun onEvent(event: DashboardScreenEvent) {
        when (event) {
            is LogoutClicked -> {}
            is RoomSelected -> _selectedRoom.update { event.roomId }
            is OnHeaderOptionClicked -> handleOptionClicked(event.option)
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
