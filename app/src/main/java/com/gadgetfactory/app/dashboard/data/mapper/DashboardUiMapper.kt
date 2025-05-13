package com.gadgetfactory.app.dashboard.data.mapper

import com.gadgetfactory.app.dashboard.model.ui.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.model.ui.DashboardScreenState

class DashboardUiMapper {
    fun map(
        headerState: DashboardHeaderUiState,
        devices: List<Int>,
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
