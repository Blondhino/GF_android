package com.gadgetfactory.app.password.ui.interaction

sealed interface PasswordScreenEvent {
    data class PasswordChanged(val password: String) : PasswordScreenEvent
    data class PasswordSubmit(val password: String) : PasswordScreenEvent
}
