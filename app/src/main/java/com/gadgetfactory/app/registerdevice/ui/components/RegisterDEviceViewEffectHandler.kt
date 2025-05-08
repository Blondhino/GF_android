package com.gadgetfactory.app.registerdevice.ui.components

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gadgetfactory.app.core.bluetooth.getRequiredBluetoothPermissions
import com.gadgetfactory.app.core.rememberBluetoothPermissionLauncher
import com.gadgetfactory.app.registerdevice.ui.RegisterDeviceViewModel
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.AdapterWarningDismissed
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.OpenAppSettingsClicked
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenEvent.PermissionErrorDismissed
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenState
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.CheckBluetoothPermission
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.OpenAppSettings
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothAdapterWarning
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothPermissionError
import com.gadgetfactory.app.registerdevice.ui.interaction.RegisterDeviceScreenViewEffect.ShowBluetoothPermissionWarning
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Error
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Warning
import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiEvent.SetBackgroundColorMode
import com.gadgetfactory.app.ui.global.snack.SnackbarController
import com.gadgetfactory.app.ui.global.snack.SnackbarMessage
import openAppSettings

@Composable
fun RegisterDeviceViewEffectHandler(
    viewModel: RegisterDeviceViewModel,
    globalUi: GlobalUi,
    uiState: RegisterDeviceScreenState,
    context: Context,
) {
    val permissionLauncher = rememberBluetoothPermissionLauncher(context) {
        viewModel.onEvent(RegisterDeviceScreenEvent.OnCheckPermissionsResult(it))
    }

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
                            duration = SnackbarDuration.Indefinite,
                            payload = uiState.bluetoothPermissionError,
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
            }
        }
    }
}
