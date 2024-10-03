package com.gadgetfactory.app.ui.global

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class GlobalUiImpl : GlobalUi {

    private val _globalUiEvent = Channel<GlobalUiEvent>(Channel.BUFFERED)
    override val globalUiEvent = _globalUiEvent.receiveAsFlow()

    override suspend fun emitUiEvent(event: GlobalUiEvent) {
        _globalUiEvent.send(event)
    }

    override fun tryEmitUiEvent(event: GlobalUiEvent) {
        _globalUiEvent.trySend(event)
    }
}
