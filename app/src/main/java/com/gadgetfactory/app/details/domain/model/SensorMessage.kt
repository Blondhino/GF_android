package com.gadgetfactory.app.details.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorMessage(
    val data: List<SensorData> = listOf(),
)

@Serializable
data class SensorData(
    val name: String,
    val unit: String,
    val value: String,
)
