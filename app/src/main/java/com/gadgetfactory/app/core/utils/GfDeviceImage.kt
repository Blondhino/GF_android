package com.gadgetfactory.app.core.utils

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.ui.components.ImageType

fun mapGFDeviceImage(deviceName: String): ImageType.Resource = when (deviceName) {
    GADGET_LUMORA -> ImageType.Resource(R.drawable.ic_lumora)
    else -> ImageType.Resource(R.drawable.ic_bluetooth)
}

const val GADGET_LUMORA = "Lumora - Air Monitor"
