package com.gadgetfactory.app.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.R
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenEvent.WiFiNetworkSelected
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Content
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.components.BodySmallText
import com.gadgetfactory.app.core.ui.components.Image
import com.gadgetfactory.app.core.ui.components.ImageType
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.ShowHeader
import com.gadgetfactory.app.core.ui.theme.SilverMist
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

@Composable
fun WiFiUiItem(
    wifiName: String,
    onWiFiClick: (wifiName: String) -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(25),
        )
        .clip(RoundedCornerShape(25))
        .background(SilverMist)
        .clickable { onWiFiClick(wifiName) }
        .padding(horizontal = 8.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
) {
    Image(imageType = ImageType.Resource(R.drawable.ic_wifi))
    BodySmallText(text = wifiName)
}
