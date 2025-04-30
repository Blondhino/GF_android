package com.gadgetfactory.app.gadgetcenter.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.gadgetcenter.model.network.RoomResponse

interface RoomRepository {
    suspend fun getRooms(): Either<NetworkError, RoomResponse>
}
