package com.gadgetfactory.app.scan.ui.components

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.connect.ui.ConnectScreen
import com.gadgetfactory.app.core.bluetooth.getRequiredBluetoothPermissions
import com.gadgetfactory.app.core.bluetooth.rememberBluetoothPermissionLauncher
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode.Error
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode.Warning
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.SetBackgroundColorMode
import com.gadgetfactory.app.core.ui.global.snack.SnackbarController
import com.gadgetfactory.app.core.ui.global.snack.SnackbarMessage
import com.gadgetfactory.app.core.utils.openAppSettings
import com.gadgetfactory.app.scan.ui.ScanViewModel
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent.AdapterWarningDismissed
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent.OpenAppSettingsClicked
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenEvent.PermissionErrorDismissed
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenState
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect.CheckBluetoothPermission
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect.OpenAppSettings
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect.ShowBluetoothAdapterWarning
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect.ShowBluetoothPermissionError
import com.gadgetfactory.app.scan.ui.interaction.ScanScreenViewEffect.ShowBluetoothPermissionWarning

@Composable
fun ScanViewEffectHandler(
    viewModel: ScanViewModel,
    globalUi: GlobalUi,
    uiState: ScanScreenState,
    context: Context,
) {
    val permissionLauncher = rememberBluetoothPermissionLauncher(context) {
        viewModel.onEvent(ScanScreenEvent.OnCheckPermissionsResult(it))
    }
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect {
            when (it) {
                is CheckBluetoothPermission -> permissionLauncher.launch(
                    getRequiredBluetoothPermissions(),
                )

                is ShowBluetoothPermissionWarning -> {
                    globalUi.tryEmitUiEvent(SetBackgroundColorMode(Warning))
                    SnackbarController.pushSnackMessage(
                        SnackbarMessage(
                            payload = uiState.bluetoothPermissionWarning,
                            onDismiss = {
                                globalUi.tryEmitUiEvent(SetBackgroundColorMode(Normal))
                                viewModel.onEvent(PermissionErrorDismissed)
                            },
                        ),
                    )
                }

                is ShowBluetoothPermissionError -> {
                    globalUi.tryEmitUiEvent(SetBackgroundColorMode(Error))
                    SnackbarController.pushSnackMessage(
                        SnackbarMessage(
                            payload = uiState.bluetoothPermissionError,
                            duration = SnackbarDuration.Indefinite,
                            onDismiss = {
                                globalUi.tryEmitUiEvent(SetBackgroundColorMode(Normal))
                                viewModel.onEvent(PermissionErrorDismissed)
                            },

                            action = { viewModel.onEvent(OpenAppSettingsClicked) },
                        ),
                    )
                }

                is OpenAppSettings -> {
                    globalUi.tryEmitUiEvent(SetBackgroundColorMode(Normal))
                    context.openAppSettings()
                }

                is ShowBluetoothAdapterWarning -> {
                    globalUi.tryEmitUiEvent(SetBackgroundColorMode(Warning))
                    SnackbarController.pushSnackMessage(
                        SnackbarMessage(
                            payload = uiState.bluetoothAdapterWarning,
                            onDismiss = {
                                globalUi.tryEmitUiEvent(SetBackgroundColorMode(Normal))
                                viewModel.onEvent(AdapterWarningDismissed)
                            },
                        ),
                    )
                }

                is ScanScreenViewEffect.GoToConnectPage -> navigator.push(
                    ConnectScreen(
                        gadgetAddress = it.gadget.address,
                        deviceName = it.gadget.name,
                    ),
                )
            }
        }
    }
}
