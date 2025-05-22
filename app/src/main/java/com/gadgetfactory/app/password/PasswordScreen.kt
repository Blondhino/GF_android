package com.gadgetfactory.app.password

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.connect.ui.components.ConnectScreenHeaderUiComponent
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode.Error
import com.gadgetfactory.app.core.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.components.PasswordTextField
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.SetBackgroundColorMode
import com.gadgetfactory.app.core.ui.global.snack.SnackbarController
import com.gadgetfactory.app.core.ui.global.snack.SnackbarMessage
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent.PasswordChanged
import com.gadgetfactory.app.password.interaction.PasswordScreenEvent.PasswordSubmit
import com.gadgetfactory.app.password.interaction.PasswordScreenState.Content
import com.gadgetfactory.app.password.interaction.PasswordScreenState.Loading
import com.gadgetfactory.app.password.interaction.PasswordScreenViewEffect.ShowSnackbar
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class PasswordScreen(
    val selectedWifiNetwork: String,
    val deviceName: String,
) : Screen {
    @Composable
    override fun Content() {
        val globalUi: GlobalUi = koinInject()
        val viewModel: PasswordViewModel =
            koinScreenModel(parameters = { parametersOf(selectedWifiNetwork, deviceName) })
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.viewEffects.collect {
                when (it) {
                    is ShowSnackbar -> {
                        globalUi.emitUiEvent(SetBackgroundColorMode(Error))
                        SnackbarController.pushSnackMessage(
                            message = SnackbarMessage(
                                payload = it.payload,
                                onDismiss = { globalUi.tryEmitUiEvent(SetBackgroundColorMode(Normal)) },
                            ),
                        )
                    }
                }
            }
        }

        when (val state = uiState) {
            is Content -> PasswordScreenContent(uiState = state, onEvent = viewModel::onEvent)
            is Loading -> {}
        }
    }
}

@Composable
fun PasswordScreenContent(
    uiState: Content,
    onEvent: (PasswordScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier = modifier.fillMaxSize()) {
    val globalUi: GlobalUi = koinInject()
    val textAlpha by animateFloatAsState(if (uiState.isTextFieldEnabled) 1f else 0.2f)

    LaunchedEffect(uiState.headerState) {
        globalUi.emitUiEvent(
            GlobalUiEvent.ShowHeader {
                ConnectScreenHeaderUiComponent(state = uiState.headerState)
            },
        )
    }

    BodyMediumText(
        text = uiState.screenMessage,
        fontWeight = SemiBold,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .alpha(textAlpha),
    )

    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        value = uiState.password,
        onValueChanged = { onEvent(PasswordChanged(it)) },
        onDone = { onEvent(PasswordSubmit(uiState.password)) },
        enabled = uiState.isTextFieldEnabled,
    )
}
