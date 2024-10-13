package com.gadgetfactory.app.auth.ui.interaction

import androidx.annotation.DrawableRes

data class AuthScreenState(
    val isLoading: Boolean,
    val isLoginContainerVisible: Boolean,
    val welcomeMessage: String,
    val appName: String,
    @DrawableRes val appImage: Int,
)
