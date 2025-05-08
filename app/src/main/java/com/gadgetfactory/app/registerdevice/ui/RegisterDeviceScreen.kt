package com.gadgetfactory.app.registerdevice.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.registerdevice.ui.components.RegisterDeviceScreenContent
import com.gadgetfactory.app.registerdevice.ui.components.RegisterDeviceViewEffectHandler
import com.gadgetfactory.app.ui.global.GlobalUi
import org.koin.compose.koinInject

class RegisterDeviceScreen : Screen {

    @Composable
    override fun Content() = Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val viewModel: RegisterDeviceViewModel = koinScreenModel()
        val globalUi: GlobalUi = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        RegisterDeviceViewEffectHandler(
            viewModel = viewModel,
            globalUi = globalUi,
            uiState = uiState,
            context = LocalContext.current,
        )

        RegisterDeviceScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEvent = viewModel::onEvent,
        )
    }
}
