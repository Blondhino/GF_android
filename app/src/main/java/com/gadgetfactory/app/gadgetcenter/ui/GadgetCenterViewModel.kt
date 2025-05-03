package com.gadgetfactory.app.gadgetcenter.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.auth.domain.usecase.Logout
import com.gadgetfactory.app.gadgetcenter.data.mapper.GadgetCenterHeaderMapper
import com.gadgetfactory.app.gadgetcenter.data.mapper.GadgetCenterUiMapper
import com.gadgetfactory.app.gadgetcenter.domain.GetGadgetCenterHeader
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterScreenState
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption.AddDevice
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption.EditRooms
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption.Notifications
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent.LogoutClicked
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent.RoomSelected
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect.HideHeader
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect.NavigateToRegisterDeviceScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GadgetCenterViewModel(
    getHeader: GetGadgetCenterHeader,
    private val uiMapper: GadgetCenterUiMapper,
    private val headerMapper: GadgetCenterHeaderMapper,
    private val logout: Logout,
) : ScreenModel {

    private val _devices = MutableStateFlow(emptyList<Int>())
    private val _selectedRoom = MutableStateFlow("")
    private val _viewEffect = Channel<GadgetCenterViewEffect>(Channel.BUFFERED)
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
        started = WhileSubscribed(5_000L),
        initialValue = GadgetCenterScreenState.Loading,
    )

    fun onEvent(event: GadgetCenterScreenEvent) {
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
                delay(150)
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
