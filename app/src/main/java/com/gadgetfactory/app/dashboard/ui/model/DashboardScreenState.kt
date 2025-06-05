package com.gadgetfactory.app.dashboard.ui.model

import com.gadgetfactory.app.dashboard.data.mapper.DevicesState

sealed interface DashboardScreenState {
    data object Loading : DashboardScreenState
    data object Error : DashboardScreenState
    data class Content(
        val headerState: DashboardHeaderUiState,
        val devices: DevicesState,
    ) : DashboardScreenState
}
