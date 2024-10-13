package com.gadgetfactory.app.auth.ui.interaction

import com.gadgetfactory.app.auth.ui.components.AuthProvider

sealed interface AuthScreenEvent {
    data object ScreenShown : AuthScreenEvent
    data object GoogleTokenFetchFailed : AuthScreenEvent
    data class GoogleAuthTokenReceived(val email: String, val token: String) : AuthScreenEvent
    data class AuthProviderSelected(val provider: AuthProvider) : AuthScreenEvent
    data object SnackDismissed : AuthScreenEvent
}
