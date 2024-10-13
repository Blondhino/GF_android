package com.gadgetfactory.app.ui.global.snack

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {

    private val _messages = Channel<SnackbarMessage>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    suspend fun pushSnackMessage(message: SnackbarMessage) {
        _messages.send(message)
    }
}
