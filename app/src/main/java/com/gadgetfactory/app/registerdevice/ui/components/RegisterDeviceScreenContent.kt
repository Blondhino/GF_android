package com.gadgetfactory.app.registerdevice.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.ScanAgainClick
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.StartScanClick
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenState
import com.gadgetfactory.app.ui.components.BodyMediumText
import com.gadgetfactory.app.ui.components.PrimaryButton
import com.gadgetfactory.app.ui.theme.SilverMist

@Composable
fun RegisterDeviceScreenContent(
    uiState: RegisterDeviceScreenState,
    onEvent: (event: RegisterDeviceScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) = Box(modifier) {
    Column {
        BluetoothScanningUiComponent(
            isScanning = uiState.isScanning,
            modifier = modifier.padding(top = 16.dp),
        )
    }

    AnimatedVisibility(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(SilverMist),
        visible = uiState.devices.isNotEmpty(),
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(delayMillis = 500)),
    ) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .padding(16.dp),
            ) {
                items(uiState.devices.size) { index ->
                    BodyMediumText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItem(),
                        textAlign = TextAlign.Center,
                        text = uiState.devices[index].name,
                        fontWeight = SemiBold,
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp),
                visible = uiState.scanAgainButtonVisible,
            ) {
                PrimaryButton(
                    onClick = { onEvent(ScanAgainClick) },
                ) {
                    BodyMediumText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = uiState.scanAgainButtonText,
                        fontWeight = SemiBold,
                    )
                }
            }
        }
    }
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp)
            .align(Alignment.BottomCenter),
        visible = uiState.isButtonVisible,
        enter = fadeIn(tween(delayMillis = 400)) + slideInVertically(tween(delayMillis = 400)),
        exit = fadeOut() + slideOutVertically(),
    ) {
        PrimaryButton(
            onClick = { onEvent(StartScanClick) },
        ) {
            BodyMediumText(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = uiState.buttonText,
                fontWeight = SemiBold,
            )
        }
    }
}
