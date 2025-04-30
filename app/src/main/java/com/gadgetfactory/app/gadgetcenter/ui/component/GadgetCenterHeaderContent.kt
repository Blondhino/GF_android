package com.gadgetfactory.app.gadgetcenter.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterHeaderUiState
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption
import com.gadgetfactory.app.ui.components.ProfileInfoUiComponent
import com.gadgetfactory.app.ui.components.TextPill

@Composable
fun GadgetCenterHeaderContent(
    uiState: GadgetCenterHeaderUiState.Content,
    modifier: Modifier = Modifier,
    onRoomClicked: (roomId: String) -> Unit = {},
    onOptionClicked: (HeaderOption) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            ProfileInfoUiComponent(uiState.profileInfo, modifier = Modifier.fillMaxWidth(0.6f))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconRoundedUiComponent(
                    data = uiState.addNewDevice,
                    onClick = { onOptionClicked(uiState.addNewDevice) },
                )

                IconRoundedUiComponent(
                    data = uiState.notificationsOption,
                    onClick = { onOptionClicked(uiState.notificationsOption) },

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
                uiState.availableRooms.forEach { room ->
                    TextPill(
                        text = room.roomName,
                        id = room.roomId,
                        isSelected = room.isSelected,
                        onClick = { onRoomClicked(room.roomId) },
                    )
                }
            }

            IconRoundedUiComponent(
                data = uiState.editRoomsOption,
                onClick = { onOptionClicked(uiState.editRoomsOption) },
            )
        }
    }
}
