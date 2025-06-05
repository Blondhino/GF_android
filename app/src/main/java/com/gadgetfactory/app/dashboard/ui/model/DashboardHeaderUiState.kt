package com.gadgetfactory.app.dashboard.ui.model

sealed class DashboardHeaderUiState {
    data object Loading : DashboardHeaderUiState()
    data class Error(val message: String) : DashboardHeaderUiState()
    data class Content(
        val profileInfo: ProfileInfoUiComponentData,
        val availableRooms: List<RoomUiComponentData>,
        val addNewDevice: HeaderOption,
        val notificationsOption: HeaderOption,
        val editRoomsOption: HeaderOption,
    ) : DashboardHeaderUiState()
}
