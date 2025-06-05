package com.gadgetfactory.app.details.ui.interaction

import com.gadgetfactory.app.details.domain.model.DeviceInfographic

sealed interface DetailsScreenState {
    data object Loading : DetailsScreenState
    data class Content(val infographics: List<DeviceInfographic>) : DetailsScreenState
    data object Error : DetailsScreenState
}
