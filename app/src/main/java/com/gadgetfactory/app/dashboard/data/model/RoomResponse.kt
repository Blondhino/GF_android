package com.gadgetfactory.app.dashboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val rooms: List<Room>?,
)

@Serializable
data class Room(
    val roomId: String?,
    val roomName: String?,
)
