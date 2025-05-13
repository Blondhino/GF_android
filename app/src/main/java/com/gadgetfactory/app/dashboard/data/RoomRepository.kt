package com.gadgetfactory.app.dashboard.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.dashboard.model.network.RoomResponse

interface RoomRepository {
    suspend fun getRooms(): Either<NetworkError, RoomResponse>
}
