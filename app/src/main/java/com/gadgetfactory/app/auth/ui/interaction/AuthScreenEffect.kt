package com.gadgetfactory.app.auth.ui.interaction

import com.gadgetfactory.app.ui.global.snack.SnackbarPayload

sealed interface AuthScreenEffect {
    data object ShowGoogleLoginDialog : AuthScreenEffect
    data object OpenGadgetCenter : AuthScreenEffect
    data class ShowSnackMessage(val payload: SnackbarPayload) : AuthScreenEffect
}
