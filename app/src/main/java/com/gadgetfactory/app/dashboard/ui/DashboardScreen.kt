package com.gadgetfactory.app.dashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.gadgetfactory.app.dashboard.data.mapper.DevicesState
import com.gadgetfactory.app.dashboard.data.mapper.DevicesState.Error
import com.gadgetfactory.app.dashboard.data.mapper.DevicesState.Loading
import com.gadgetfactory.app.dashboard.ui.component.DeviceUiItemComponent
import com.gadgetfactory.app.dashboard.ui.component.GadgetCenterHeaderContent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnDeviceSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.OnHeaderOptionClicked
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardScreenEvent.RoomSelected
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToAuthScreen
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToDetails
import com.gadgetfactory.app.dashboard.ui.interaction.DashboardViewEffect.NavigateToRegisterDeviceScreen
import com.gadgetfactory.app.dashboard.ui.model.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.ui.model.DashboardScreenState
import com.gadgetfactory.app.dashboard.ui.model.DashboardScreenState.Content
import com.gadgetfactory.app.details.ui.DetailsScreen
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
                    is DashboardViewEffect.HideHeader -> globalUi.emitUiEvent(HideHeader)
                    is NavigateToAuthScreen -> navigator.replaceAll(AuthScreen())
                    is NavigateToRegisterDeviceScreen -> navigator.push(ScanScreen())
                    is NavigateToDetails -> navigator.push(
                        DetailsScreen(
                            userId = effect.userId,
                            deviceName = effect.deviceName,
                        ),
                    )
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
    Box(modifier = Modifier.fillMaxSize()) {
        when (val devicesState = uiState.devices) {
            is DevicesState.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(devicesState.devices) {
                        DeviceUiItemComponent(
                            device = it,
                            onDeviceClick = { device -> onEvent(OnDeviceSelected(device)) },
                        )
                    }
                }
            }

            is Error -> BodyMediumText("Error loading devices")
            is Loading -> RoundLoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
