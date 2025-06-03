package com.gadgetfactory.app.dashboard.data.mapper

import com.gadgetfactory.app.dashboard.ui.model.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.ui.model.DashboardScreenState

class DashboardUiMapper {
    fun map(
        headerState: DashboardHeaderUiState,
        devices: DevicesState,
    ): DashboardScreenState =
        when (headerState) {
            is DashboardHeaderUiState.Content -> DashboardScreenState.Content(
                headerState = headerState,
                devices = devices,
            )

            is DashboardHeaderUiState.Error -> DashboardScreenState.Error
            is DashboardHeaderUiState.Loading -> DashboardScreenState.Loading
        }
}
