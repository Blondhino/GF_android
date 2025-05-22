package com.gadgetfactory.app.password.interaction

sealed interface PasswordScreenEvent {
    data class PasswordChanged(val password: String) : PasswordScreenEvent
    data class PasswordSubmit(val password: String) : PasswordScreenEvent
}
