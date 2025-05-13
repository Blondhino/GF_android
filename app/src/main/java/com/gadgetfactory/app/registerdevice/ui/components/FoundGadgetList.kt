package com.gadgetfactory.app.registerdevice.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.ScanAgainClick
import com.gadgetfactory.app.ui.components.BodySmallText
import com.gadgetfactory.app.ui.components.SheetHandle
import com.gadgetfactory.app.ui.theme.SilverMist

@Composable
fun FoundGadgetsList(
    devices: List<FoundGadget>,
    scanAgainButtonVisible: Boolean,
    onEvent: (event: RegisterDeviceScreenEvent) -> Unit,
    scanAgainButtonText: String,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .fillMaxWidth()
            .background(SilverMist),
        visible = devices.isNotEmpty(),
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(delayMillis = 500)),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SheetHandle(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(8.dp),
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .padding(16.dp),
            ) {
                items(devices.size) { index ->
                    FoundGadgetUiItem(
                        modifier = Modifier.animateItem(),
                        gadget = devices[index],
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                visible = scanAgainButtonVisible,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    BodySmallText(
                        stringResource(R.string.device_not_found_question),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    BodySmallText(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25))
                            .clickable { onEvent(ScanAgainClick) }
                            .padding(4.dp),
                        text = scanAgainButtonText,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = SemiBold,
                    )
                }
            }
        }
    }
}
