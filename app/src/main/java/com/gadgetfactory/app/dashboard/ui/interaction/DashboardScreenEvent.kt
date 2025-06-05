package com.gadgetfactory.app.dashboard.ui.interaction

import com.gadgetfactory.app.dashboard.ui.model.DeviceUiItem
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption

sealed interface DashboardScreenEvent {
    data class RoomSelected(val roomId: String) : DashboardScreenEvent
    data class OnHeaderOptionClicked(val option: HeaderOption) : DashboardScreenEvent
    data object LogoutClicked : DashboardScreenEvent
    data class OnDeviceSelected(val device: DeviceUiItem) : DashboardScreenEvent
}
