package com.gadgetfactory.app.password.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    val id: String,
    val mac: String,
    val type: String,
    val apiKey: String,
)
