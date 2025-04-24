package com.gadgetfactory.app.gadgetcenter

import cafe.adriel.voyager.core.model.ScreenModel
import com.gadgetfactory.app.R
import com.gadgetfactory.app.gadgetcenter.component.GadgetCenterHeaderContentData
import com.gadgetfactory.app.gadgetcenter.model.RoomUiComponentData
import com.gadgetfactory.app.ui.components.IconRoundedUiComponentData
import com.gadgetfactory.app.ui.components.ProfileInfoUiComponentData

class GadgetCenterViewModel : ScreenModel {

    fun getHeaderData() = GadgetCenterHeaderContentData(
        profileInfo = ProfileInfoUiComponentData(
            profileImageUrl = "",
            profileTitle = "Enio's Home",
        ),
        availableRooms = listOf(
            RoomUiComponentData(
                roomName = "All",
                roomId = "all",
                isSelected = true,
            ),
            RoomUiComponentData(
                roomName = "Living Room",
                roomId = "living_room",
                isSelected = false,
            ),
            RoomUiComponentData(
                roomName = "Bedroom",
                roomId = "bedroom",
                isSelected = false,
            ),
            RoomUiComponentData(
                roomName = "Kitchen",
                roomId = "kitchen",
                isSelected = false,
            ),
        ),

        addNewRoomOption = IconRoundedUiComponentData(
            icon = R.drawable.ic_add,
            id = "add_new_room",
        ),

        notificationsOption = IconRoundedUiComponentData(
            icon = R.drawable.ic_notification,
            id = "add_new_room",
        ),
        editRoomsOption = IconRoundedUiComponentData(
            icon = R.drawable.ic_edit,
            id = "add_new_room",
        ),
    )
}
