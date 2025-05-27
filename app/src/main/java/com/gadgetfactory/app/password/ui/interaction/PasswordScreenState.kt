package com.gadgetfactory.app.password.ui.interaction

import com.gadgetfactory.app.connect.ui.interaction.ConnectScreenHeaderState

sealed interface PasswordScreenState {
    data class Content(
        val screenMessage: String,
        val password: String,
        val hint: String,
        val isTextFieldEnabled: Boolean,
        val headerState: ConnectScreenHeaderState,
    ) : PasswordScreenState

    data object Loading : PasswordScreenState
}
