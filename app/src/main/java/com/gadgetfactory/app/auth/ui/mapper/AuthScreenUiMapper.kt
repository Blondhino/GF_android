package com.gadgetfactory.app.auth.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.auth.ui.interaction.AuthScreenState
import com.gadgetfactory.app.core.dictionary.Dictionary

class AuthScreenUiMapper(
    private val dictionary: Dictionary,
) {
    fun map() = AuthScreenState(
        isLoading = false,
        isLoginContainerVisible = false,
        welcomeMessage = dictionary.getString(R.string.auth_screen_welcome_message),
        appName = dictionary.getString(R.string.general_app_name),
        appImage = R.drawable.ic_app_logo,
    )
}
