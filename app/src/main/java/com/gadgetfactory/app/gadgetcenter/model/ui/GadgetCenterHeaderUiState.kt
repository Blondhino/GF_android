package com.gadgetfactory.app.gadgetcenter.model.ui

sealed class GadgetCenterHeaderUiState {
    data object Loading : GadgetCenterHeaderUiState()
    data class Error(val message: String) : GadgetCenterHeaderUiState()
    data class Content(
        val profileInfo: ProfileInfoUiComponentData,
        val availableRooms: List<RoomUiComponentData>,
        val addNewDevice: HeaderOption,
        val notificationsOption: HeaderOption,
        val editRoomsOption: HeaderOption,
    ) : GadgetCenterHeaderUiState()
}
