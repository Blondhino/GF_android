package com.gadgetfactory.app.gadgetcenter.model.ui

sealed interface GadgetCenterScreenState {
    data object Loading : GadgetCenterScreenState
    data object Error : GadgetCenterScreenState
    data class Content(
        val headerState: GadgetCenterHeaderUiState,
        val devices: List<Int>,
    ) : GadgetCenterScreenState
}
