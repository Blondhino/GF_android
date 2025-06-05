package com.gadgetfactory.app.dashboard.ui.model

import com.gadgetfactory.app.core.devices.DeviceType
import com.gadgetfactory.app.core.ui.components.ImageType

data class DeviceUiItem(
    val id: String,
    val mac: String,
    val type: DeviceType,
    val name: String,
    val image: ImageType.Resource,
)
