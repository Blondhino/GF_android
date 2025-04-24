package com.gadgetfactory.app.gadgetcenter.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.gadgetcenter.model.RoomUiComponentData
import com.gadgetfactory.app.ui.components.IconRoundedUiComponent
import com.gadgetfactory.app.ui.components.IconRoundedUiComponentData
import com.gadgetfactory.app.ui.components.ProfileInfoUiComponent
import com.gadgetfactory.app.ui.components.ProfileInfoUiComponentData
import com.gadgetfactory.app.ui.components.TextPill

@Composable
fun GadgetCenterHeaderContent(
    data: GadgetCenterHeaderContentData,
    modifier: Modifier = Modifier,
    onRoomClicked: (roomId: String) -> Unit = {},
    onOptionClicked: (optionId: String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            ProfileInfoUiComponent(data.profileInfo, modifier = Modifier.fillMaxWidth(0.6f))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconRoundedUiComponent(
                    data = data.addNewRoomOption,
                    onClick = { onOptionClicked(data.addNewRoomOption.id) },
                )

                IconRoundedUiComponent(
                    data = data.notificationsOption,
                    onClick = { onOptionClicked(data.notificationsOption.id) },

                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(0.85f),
            ) {
                data.availableRooms.forEach { room ->
                    TextPill(
                        text = room.roomName,
                        id = room.roomId,
                        isSelected = room.isSelected,
                        onClick = { onRoomClicked(room.roomId) },
                    )
                }
            }

            IconRoundedUiComponent(
                data = data.editRoomsOption,
                onClick = { onOptionClicked(data.editRoomsOption.id) },
            )
        }
    }
}

data class GadgetCenterHeaderContentData(
    val profileInfo: ProfileInfoUiComponentData,
    val availableRooms: List<RoomUiComponentData>,
    val addNewRoomOption: IconRoundedUiComponentData,
    val notificationsOption: IconRoundedUiComponentData,
    val editRoomsOption: IconRoundedUiComponentData,
)
