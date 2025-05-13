package com.gadgetfactory.app.connect

import cafe.adriel.voyager.core.model.ScreenModel
import com.gadgetfactory.app.connect.interaction.ConnectScreenEvent
import com.gadgetfactory.app.connect.interaction.ConnectScreenEvent.ConnectScreenEvent1

class ConnectViewModel : ScreenModel {

    fun onEvent(event: ConnectScreenEvent) {
        when (event) {
            is ConnectScreenEvent1 -> {}
        }
    }
}
