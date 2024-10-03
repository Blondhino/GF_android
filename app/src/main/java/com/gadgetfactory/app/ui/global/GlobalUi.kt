package com.gadgetfactory.app.ui.global

import kotlinx.coroutines.flow.Flow

interface GlobalUi {
    val globalUiEvent: Flow<GlobalUiEvent>
    suspend fun emitUiEvent(event: GlobalUiEvent)
    fun tryEmitUiEvent(event: GlobalUiEvent)
}
