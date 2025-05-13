package com.gadgetfactory.app.dashboard.model.ui

sealed interface DashboardScreenState {
    data object Loading : DashboardScreenState
    data object Error : DashboardScreenState
    data class Content(
        val headerState: DashboardHeaderUiState,
        val devices: List<Int>,
    ) : DashboardScreenState
}
