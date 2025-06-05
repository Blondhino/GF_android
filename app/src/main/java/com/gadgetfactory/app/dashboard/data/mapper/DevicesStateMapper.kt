package com.gadgetfactory.app.dashboard.data.mapper

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.dashboard.ui.mapper.DeviceUiItemMapper
import com.gadgetfactory.app.dashboard.ui.model.DeviceUiItem
import com.gadgetfactory.app.password.domain.repo.DeviceDto

class DevicesStateMapper(
    private val deviceUiItemMapper: DeviceUiItemMapper,
) {
    fun map(
        devicesResponse: Either<NetworkError, List<DeviceDto>>,
    ): DevicesState = devicesResponse.fold(
        ifLeft = { DevicesState.Error },
        ifRight = { DevicesState.Content(deviceUiItemMapper.map(it)) },
    )
}

sealed interface DevicesState {
    data object Loading : DevicesState
    data class Content(val devices: List<DeviceUiItem>) : DevicesState
    data object Error : DevicesState
}
