package com.gadgetfactory.app.core.devices

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.devices.DeviceType.Lumora
import com.gadgetfactory.app.core.ui.components.ImageType

sealed class DeviceType(val shortName: String) {
    data object Lumora : DeviceType("lumora")
}

fun mapGFDeviceImage(deviceName: String): ImageType.Resource = when (deviceName) {
    GADGET_LUMORA -> ImageType.Resource(R.drawable.ic_lumora)
    else -> ImageType.Resource(R.drawable.ic_bluetooth)
}

fun getDeviceType(shortName: String): DeviceType = when (shortName) {
    Lumora.shortName -> Lumora
    else -> throw IllegalArgumentException("Unknown device type: $shortName")
}

fun getFullDeviceName(
    shortName: String,
): String = when (shortName) {
    Lumora.shortName -> GADGET_LUMORA
    else -> throw IllegalArgumentException("Unknown device type: $shortName")
}

const val GADGET_LUMORA = "Lumora - Air Monitor"
