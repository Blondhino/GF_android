package com.gadgetfactory.app.auth.domain.model

sealed interface AuthError {
    data object UnknownAuthError : AuthError
    data class CredentialsNotReceived(val errorMessage: String) : AuthError
    data object UserNotFound : AuthError
}
