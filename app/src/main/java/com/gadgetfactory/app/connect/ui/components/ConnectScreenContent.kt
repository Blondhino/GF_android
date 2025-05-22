package com.gadgetfactory.app.connect.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent.WiFiNetworkSelected
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Content
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.ShowHeader
import org.koin.compose.koinInject

@Composable
fun ConnectScreenContent(
    state: Content,
    onEvent: (ConnectScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier = modifier.fillMaxSize()) {
    val globalUi: GlobalUi = koinInject()
    LaunchedEffect(state.headerState) {
        globalUi.emitUiEvent(ShowHeader { ConnectScreenHeaderUiComponent(state.headerState) })
    }

    BodyMediumText(
        text = state.screenMessage,
        fontWeight = SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
    )

    LazyColumn {
        items(state.availableNetworks.size) { index ->
            WiFiUiItem(
                wifiName = state.availableNetworks[index],
                onWiFiClick = { onEvent(WiFiNetworkSelected(it)) },
                modifier = Modifier
                    .animateItem()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}
