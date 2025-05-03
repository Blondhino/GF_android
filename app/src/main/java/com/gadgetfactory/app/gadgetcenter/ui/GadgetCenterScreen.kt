package com.gadgetfactory.app.gadgetcenter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.auth.ui.AuthScreen
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterHeaderUiState
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterScreenState
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterScreenState.Content
import com.gadgetfactory.app.gadgetcenter.ui.component.GadgetCenterHeaderContent
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterScreenEvent.RoomSelected
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.gadgetcenter.ui.interaction.GadgetCenterViewEffect.NavigateToRegisterDeviceScreen
import com.gadgetfactory.app.registerdevice.RegisterDeviceScreen
import com.gadgetfactory.app.ui.components.BodyMediumText
import com.gadgetfactory.app.ui.components.RoundLoadingIndicator
import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiEvent.HideHeader
import com.gadgetfactory.app.ui.global.GlobalUiEvent.ShowHeader
import org.koin.compose.koinInject

class GadgetCenterScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val globalUi: GlobalUi = koinInject()
        val viewModel: GadgetCenterViewModel = koinScreenModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.viewEffect.collect { effect ->
                when (effect) {
                    GadgetCenterViewEffect.HideHeader -> globalUi.emitUiEvent(HideHeader)
                    NavigateToAuthScreen -> navigator.replaceAll(AuthScreen())
                    NavigateToRegisterDeviceScreen -> navigator.push(RegisterDeviceScreen())
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is Content -> ScreenContent(state, globalUi, viewModel::onEvent)
                is GadgetCenterScreenState.Error -> BodyMediumText(
                    text = "Error",
                    modifier = Modifier.align(Alignment.Center),
                )

                is GadgetCenterScreenState.Loading -> {
                    RoundLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(
    uiState: Content,
    globalUi: GlobalUi,
    onEvent: (GadgetCenterScreenEvent) -> Unit,
) {
    when (uiState.headerState) {
        is GadgetCenterHeaderUiState.Content -> {
            globalUi.tryEmitUiEvent(
                ShowHeader(
                    content = {
                        GadgetCenterHeaderContent(
                            uiState = uiState.headerState,
                            onRoomClicked = { onEvent(RoomSelected(it)) },
                            onOptionClicked = { onEvent(OnHeaderOptionClicked(it)) },
                        )
                    },
                ),
            )
        }

        else -> globalUi.tryEmitUiEvent(HideHeader)
    }
}
