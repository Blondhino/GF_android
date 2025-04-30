package com.gadgetfactory.app.gadgetcenter.data.mapper

import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterHeaderUiState
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterScreenState

class GadgetCenterUiMapper {
    fun map(
        headerState: GadgetCenterHeaderUiState,
        devices: List<Int>,
    ): GadgetCenterScreenState =
        when (headerState) {
            is GadgetCenterHeaderUiState.Content -> GadgetCenterScreenState.Content(
                headerState = headerState,
                devices = devices,
            )

            is GadgetCenterHeaderUiState.Error -> GadgetCenterScreenState.Error
            is GadgetCenterHeaderUiState.Loading -> GadgetCenterScreenState.Loading
        }
}
