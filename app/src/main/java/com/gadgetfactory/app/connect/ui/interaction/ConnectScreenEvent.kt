package com.gadgetfactory.app.connect.ui.interaction

sealed interface ConnectScreenEvent {
    data class WiFiNetworkSelected(val ssid: String) : ConnectScreenEvent
}
