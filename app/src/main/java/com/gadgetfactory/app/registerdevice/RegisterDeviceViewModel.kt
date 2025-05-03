package com.gadgetfactory.app.registerdevice

import android.Manifest
import android.os.Build
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.registerdevice.interaction.RegisterDeviceScreenEvent
import com.gadgetfactory.app.registerdevice.interaction.RegisterDeviceScreenEvent.ScreenShown
import com.gadgetfactory.app.registerdevice.interaction.RegisterDeviceScreenViewEffect
import com.gadgetfactory.app.registerdevice.interaction.RegisterDeviceScreenViewEffect.CheckBluetoothPermission
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterDeviceViewModel : ScreenModel {

    private val _viewEffect = Channel<RegisterDeviceScreenViewEffect>(Channel.BUFFERED)
    val viewEffect = _viewEffect.receiveAsFlow()

    fun onEvent(event: RegisterDeviceScreenEvent) {
        when (event) {
            is ScreenShown -> screenModelScope.launch { _viewEffect.send(CheckBluetoothPermission) }
        }
    }

    fun getRequiredPermissions(): Array<String> {
        val basicPermissions = listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        )
        val additionPermissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            )

            else -> emptyList()
        }
        return (basicPermissions + additionPermissions).toTypedArray()
    }
}
