package com.gadgetfactory.app.dashboard.ui.mapper

import com.gadgetfactory.app.core.devices.getDeviceType
import com.gadgetfactory.app.core.devices.getFullDeviceName
import com.gadgetfactory.app.core.devices.mapGFDeviceImage
import com.gadgetfactory.app.dashboard.ui.model.DeviceUiItem
import com.gadgetfactory.app.password.domain.repo.DeviceDto

class DeviceUiItemMapper {
    fun map(
        devices: List<DeviceDto>,
    ): List<DeviceUiItem> = devices.map { it.toUiItem() }

    private fun DeviceDto.toUiItem(): DeviceUiItem = DeviceUiItem(
        id = id,
        mac = mac,
        type = getDeviceType(this.type),
        name = getFullDeviceName(this.type),
        image = mapGFDeviceImage(getFullDeviceName(this.type)),
    )
}
