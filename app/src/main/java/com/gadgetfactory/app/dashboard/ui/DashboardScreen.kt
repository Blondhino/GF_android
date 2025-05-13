package com.gadgetfactory.app.dashboard.ui

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
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.components.RoundLoadingIndicator
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.HideHeader
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.ShowHeader
import com.gadgetfactory.app.dashboard.model.ui.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.model.ui.DashboardScreenState
import com.gadgetfactory.app.dashboard.model.ui.DashboardScreenState.Content
import com.gadgetfactory.app.dashboard.ui.component.GadgetCenterHeaderContent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.RoomSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToRegisterDeviceScreen
import com.gadgetfactory.app.scan.ui.ScanScreen
import org.koin.compose.koinInject

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val globalUi: GlobalUi = koinInject()
        val viewModel: DashboardViewModel = koinScreenModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.viewEffect.collect { effect ->
                when (effect) {
                    DashboardViewEffect.HideHeader -> globalUi.emitUiEvent(HideHeader)
                    NavigateToAuthScreen -> navigator.replaceAll(AuthScreen())
                    NavigateToRegisterDeviceScreen -> navigator.push(ScanScreen())
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is Content -> ScreenContent(state, globalUi, viewModel::onEvent)
                is DashboardScreenState.Error -> BodyMediumText(
                    text = "Error",
                    modifier = Modifier.align(Alignment.Center),
                )

                is DashboardScreenState.Loading -> {
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
    onEvent: (DashboardScreenEvent) -> Unit,
) {
    when (uiState.headerState) {
        is DashboardHeaderUiState.Content -> {
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
