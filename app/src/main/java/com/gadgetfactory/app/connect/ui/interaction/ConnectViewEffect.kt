package com.gadgetfactory.app.connect.ui.interaction

sealed interface ConnectViewEffect {
    data object HideHeader : ConnectViewEffect
}
