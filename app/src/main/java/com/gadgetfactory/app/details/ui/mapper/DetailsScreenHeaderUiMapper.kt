package com.gadgetfactory.app.details.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.devices.mapGFDeviceImage
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenHeaderState

class DetailsScreenHeaderUiMapper(
    val dictionary: Dictionary,
) {
    fun map(
        isLoading: Boolean,
        deviceName: String,
    ): DetailsScreenHeaderState = DetailsScreenHeaderState(
        deviceImage = mapGFDeviceImage(deviceName),
        isLoading = isLoading,
        deviceName = deviceName,
        message = if (isLoading) {
            dictionary.getString(R.string.waiting_for_device_title)
        } else {
            dictionary.getString(R.string.connected_with_device_title)
        },
    )
}
