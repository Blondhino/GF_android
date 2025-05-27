package com.gadgetfactory.app.connect.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.connect.ui.components.ConnectScreenContent
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Content
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Error
import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenState.Loading
import com.gadgetfactory.app.connect.ui.interaction.ConnectViewEffect.OpenPasswordScreen
import com.gadgetfactory.app.core.ui.components.BodySmallText
import com.gadgetfactory.app.core.ui.components.RoundLoadingIndicator
import com.gadgetfactory.app.password.ui.PasswordScreen
import org.koin.core.parameter.parametersOf

class ConnectScreen(
    private val gadgetAddress: String,
    private val deviceName: String,
) : Screen {
    @Composable
    override fun Content() = Box(modifier = Modifier.fillMaxSize()) {
        val viewModel: ConnectViewModel = koinScreenModel(
            parameters = { parametersOf(deviceName, gadgetAddress) },
        )
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.viewEffects.collect { effect ->
                when (effect) {
                    is OpenPasswordScreen -> navigator.push(
                        PasswordScreen(
                            selectedWifiNetwork = effect.selectedWiFi,
                            deviceName = deviceName,
                        ),
                    )
                }
            }
        }
        when (val state = uiState) {
            is Content -> ConnectScreenContent(state = state, onEvent = viewModel::onEvent)
            is Error -> BodySmallText("Error")
            is Loading -> RoundLoadingIndicator()
        }
    }
}
