package com.gadgetfactory.app.auth.ui

import arrow.core.Either.Left
import arrow.core.Either.Right
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.auth.domain.usecase.LoginWithGoogle
import com.gadgetfactory.app.auth.ui.components.AuthProvider
import com.gadgetfactory.app.auth.ui.components.AuthProvider.Google
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.OpenGadgetCenter
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.ShowGoogleLoginDialog
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEffect.ShowSnackMessage
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.AuthProviderSelected
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.GoogleAuthTokenReceived
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.GoogleTokenFetchFailed
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.ScreenShown
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenEvent.SnackDismissed
import com.gadgetfactory.app.auth.ui.mapper.AuthScreenUiMapper
import com.gadgetfactory.app.auth.ui.mapper.GoogleLoginSnackErrorMapper
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Error
import com.gadgetfactory.app.ui.components.BackgroundColorMode.Normal
import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiEvent.SetBackgroundColorMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val globalUi: GlobalUi,
    private val loginWithGoogle: LoginWithGoogle,
    private val loginErrorMapper: GoogleLoginSnackErrorMapper,
    uiMapper: AuthScreenUiMapper,
) : ScreenModel {

    private val _viewEffect = Channel<AuthScreenEffect>(Channel.BUFFERED)
    val viewEffect: Flow<AuthScreenEffect> = _viewEffect.receiveAsFlow()
    private val _uiState = MutableStateFlow(uiMapper.map())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is GoogleAuthTokenReceived -> continueWithGoogle(event)
            is AuthProviderSelected -> handleAuthProviderSelected(event.provider)
            is ScreenShown -> _uiState.update { it.copy(isLoginContainerVisible = true) }
            is GoogleTokenFetchFailed -> setUiToErrorMode()
            is SnackDismissed -> setUiToNormalMode()
        }
    }

    private fun setUiToErrorMode() = screenModelScope.launch {
        _uiState.update { it.copy(isLoginContainerVisible = false) }
        globalUi.emitUiEvent(SetBackgroundColorMode(Error))
        showErrorSnack()
    }

    private fun setUiToNormalMode() = screenModelScope.launch {
        _uiState.update { it.copy(isLoginContainerVisible = true) }
        globalUi.emitUiEvent(SetBackgroundColorMode(Normal))
    }

    private fun showErrorSnack() = screenModelScope.launch {
        _viewEffect.send(ShowSnackMessage(loginErrorMapper.map()))
    }

    private fun continueWithGoogle(event: GoogleAuthTokenReceived) = screenModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isLoginContainerVisible = false) }
        when (loginWithGoogle(email = event.email, token = event.token)) {
            is Left -> setUiToErrorMode()
            is Right -> _viewEffect.send(OpenGadgetCenter)
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun handleAuthProviderSelected(provider: AuthProvider) = screenModelScope.launch {
        when (provider) {
            is Google -> _viewEffect.send(ShowGoogleLoginDialog)
        }
    }
}
