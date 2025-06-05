package com.gadgetfactory.app.details.ui.interaction

import com.gadgetfactory.app.core.ui.components.ImageType

data class DetailsScreenHeaderState(
    val deviceImage: ImageType.Resource,
    val isLoading: Boolean,
    val deviceName: String,
    val message: String,
)
