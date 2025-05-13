package com.gadgetfactory.app.scan.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent.StartScanClick
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenState
import com.gadgetfactory.app.ui.components.BodyMediumText
import com.gadgetfactory.app.ui.components.PrimaryButton

@Composable
fun ScanScreenContent(
    uiState: ScanScreenState,
    onEvent: (event: ScanScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) = Box(modifier) {
    Column {
        BluetoothScanningUiComponent(
            isScanning = uiState.isScanning,
            modifier = modifier.padding(top = 16.dp),
        )
    }

    FoundGadgetsList(
        modifier = Modifier.align(Alignment.BottomCenter),
        devices = uiState.devices,
        scanAgainButtonVisible = uiState.scanAgainButtonVisible,
        scanAgainButtonText = uiState.scanAgainButtonText,
        onEvent = onEvent,
    )

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
