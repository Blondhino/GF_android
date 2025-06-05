package com.gadgetfactory.app.connect.ui.interaction

sealed interface ConnectViewEffect {
    data class OpenPasswordScreen(val selectedWiFi: String) : ConnectViewEffect
}
