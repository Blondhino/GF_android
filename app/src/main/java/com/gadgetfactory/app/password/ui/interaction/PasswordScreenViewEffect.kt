package com.gadgetfactory.app.password.ui.interaction

import com.gadgetfactory.app.core.ui.global.snack.SnackbarPayload

sealed interface PasswordScreenViewEffect {
    data class ShowSnackbar(val payload: SnackbarPayload) : PasswordScreenViewEffect
    data object GoToDashboard : PasswordScreenViewEffect
    data object SetErrorBackground : PasswordScreenViewEffect
    data object SetSuccessBackground : PasswordScreenViewEffect
}
