package com.gadgetfactory.app.password.domain.repo

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String,
    val mac: String,
    val type: String,
    val roomId: String,
)
