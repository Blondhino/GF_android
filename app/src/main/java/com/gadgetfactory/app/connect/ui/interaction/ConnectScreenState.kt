package com.gadgetfactory.app.connect.ui.interaction

import com.gadgetfactory.app.core.ui.components.ImageType

sealed interface ConnectScreenState {
    data object Loading : ConnectScreenState
    data class Content(
        val headerState: ConnectScreenHeaderState,
        val availableNetworks: List<String>,
        val screenMessage: String,
    ) : ConnectScreenState

    data object Error : ConnectScreenState
}

data class ConnectScreenHeaderState(
    val deviceImage: ImageType.Resource,
    val isLoading: Boolean,
    val deviceName: String,
    val message: String,
)
