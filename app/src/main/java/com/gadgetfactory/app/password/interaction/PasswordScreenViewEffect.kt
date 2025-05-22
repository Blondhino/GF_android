package com.gadgetfactory.app.password.interaction

import com.gadgetfactory.app.core.ui.global.snack.SnackbarPayload

sealed interface PasswordScreenViewEffect {
    data class ShowSnackbar(val payload: SnackbarPayload) : PasswordScreenViewEffect
}
