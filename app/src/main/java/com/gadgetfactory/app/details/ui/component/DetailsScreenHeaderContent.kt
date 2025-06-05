package com.gadgetfactory.app.details.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.connect.ui.components.ConnectingDeviceUiHeaderItem
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenHeaderState

@Composable
fun DetailsScreenHeaderContent(
    state: DetailsScreenHeaderState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ConnectingDeviceUiHeaderItem(
            deviceName = state.deviceName,
            image = state.deviceImage,
            headerMessage = state.message,
        )

        AnimatedVisibility(state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
