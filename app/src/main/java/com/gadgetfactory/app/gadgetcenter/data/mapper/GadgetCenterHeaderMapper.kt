package com.gadgetfactory.app.gadgetcenter.data.mapper

import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterHeaderUiState
import com.gadgetfactory.app.gadgetcenter.model.ui.RoomUiComponentData

class GadgetCenterHeaderMapper {
    fun map(
        headerState: GadgetCenterHeaderUiState,
        selectedRoom: String,
    ): GadgetCenterHeaderUiState =
        when (headerState) {
            is GadgetCenterHeaderUiState.Content -> headerState.copy(
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
