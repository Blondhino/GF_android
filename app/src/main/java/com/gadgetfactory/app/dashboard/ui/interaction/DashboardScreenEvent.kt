package com.gadgetfactory.app.dashboard.ui.interaction

import com.gadgetfactory.app.dashboard.model.ui.HeaderOption

sealed interface DashboardScreenEvent {
    data class RoomSelected(val roomId: String) : DashboardScreenEvent
    data class OnHeaderOptionClicked(val option: HeaderOption) : DashboardScreenEvent
    data object LogoutClicked : DashboardScreenEvent
}
