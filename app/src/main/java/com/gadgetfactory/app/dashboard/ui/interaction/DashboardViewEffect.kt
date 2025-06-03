package com.gadgetfactory.app.dashboard.ui.interaction

sealed interface DashboardViewEffect {
    data object NavigateToAuthScreen : DashboardViewEffect
    data object NavigateToRegisterDeviceScreen : DashboardViewEffect
    data class NavigateToDetails(val deviceId: String, val userId: String) : DashboardViewEffect
    data object HideHeader : DashboardViewEffect
}
