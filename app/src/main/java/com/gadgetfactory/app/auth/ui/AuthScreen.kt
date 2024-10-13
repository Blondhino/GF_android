package com.gadgetfactory.app.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.auth.domain.usecase.GetGoogleCredential
import com.gadgetfactory.app.auth.ui.components.LoginButtonData.GoogleLoginButtonData
import com.gadgetfactory.app.auth.ui.components.LoginContainer
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.OpenGadgetCenter
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.ShowGoogleLoginDialog
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.ShowSnackMessage
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.AuthProviderSelected
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.GoogleAuthTokenReceived
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.GoogleTokenFetchFailed
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.SnackDismissed
import com.gadgetfactory.app.gadgetcenter.GadgetCenterScreen
import com.gadgetfactory.app.ui.components.BodyLargeText
import com.gadgetfactory.app.ui.components.BodySmallText
import com.gadgetfactory.app.ui.components.Image
import com.gadgetfactory.app.ui.components.ImageType
import com.gadgetfactory.app.ui.components.RoundLoadingIndicator
import com.gadgetfactory.app.ui.global.snack.SnackbarController
import com.gadgetfactory.app.ui.global.snack.SnackbarMessage
import org.koin.compose.koinInject

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = koinInject()
        val getGoogleCredential: GetGoogleCredential = koinInject()
        val context = LocalContext.current
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.onEvent(AuthScreenEvent.ScreenShown)
            viewModel.viewEffect.collect {
                when (it) {
                    ShowGoogleLoginDialog -> {
                        getGoogleCredential(context).onRight {
                            viewModel.onEvent(
                                GoogleAuthTokenReceived(email = it.email, token = it.token),
                            )
                        }.onLeft { viewModel.onEvent(GoogleTokenFetchFailed) }
                    }

                    OpenGadgetCenter -> navigator.replace(GadgetCenterScreen())
                    is ShowSnackMessage -> {
                        SnackbarController.pushSnackMessage(
                            message = SnackbarMessage(
                                payload = it.payload,
                                onDismiss = { viewModel.onEvent(SnackDismissed) },
                            ),
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (uiState.isLoading) {
                RoundLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 180.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Image(imageType = ImageType.Resource(uiState.appImage))
                Spacer(modifier = Modifier.height(8.dp))
                BodySmallText(
                    text = uiState.welcomeMessage,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                BodyLargeText(text = uiState.appName, fontWeight = FontWeight.SemiBold)
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = uiState.isLoginContainerVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300),
                ),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                LoginContainer(
                    buttonsData = listOf(GoogleLoginButtonData()),
                    onProviderClick = { viewModel.onEvent(AuthProviderSelected(it)) },
                )
            }
        }
    }
}
