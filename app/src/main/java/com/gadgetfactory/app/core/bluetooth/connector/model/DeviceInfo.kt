package com.gadgetfactory.app.core.bluetooth.connector.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceInfo(
    val mac: String,
    val type: String,
)
