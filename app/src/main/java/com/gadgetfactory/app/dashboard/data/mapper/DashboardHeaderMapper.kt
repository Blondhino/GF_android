package com.gadgetfactory.app.dashboard.data.mapper

import com.gadgetfactory.app.dashboard.model.ui.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.model.ui.RoomUiComponentData

class DashboardHeaderMapper {
    fun map(
        headerState: DashboardHeaderUiState,
        selectedRoom: String,
    ): DashboardHeaderUiState =
        when (headerState) {
            is DashboardHeaderUiState.Content -> headerState.copy(
                availableRooms =
                listOf(
                    RoomUiComponentData(
                        roomName = "All",
                        roomId = "",
                        isSelected = selectedRoom == "",
                    ),
                ).plus(
                    headerState.availableRooms
                        .map { room ->
                            room.copy(isSelected = room.roomId == selectedRoom)
                        },
                ),
            )

            else -> headerState
        }
}
