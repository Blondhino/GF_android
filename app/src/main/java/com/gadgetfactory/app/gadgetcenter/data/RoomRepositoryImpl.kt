package com.gadgetfactory.app.gadgetcenter.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.core.networking.safeApiCall
import com.gadgetfactory.app.core.routes.V1
import com.gadgetfactory.app.gadgetcenter.model.network.RoomResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

class RoomRepositoryImpl(
    private val client: HttpClient,
) : RoomRepository {
    override suspend fun getRooms(): Either<NetworkError, RoomResponse> = safeApiCall {
        client.get(V1.GetRooms()).body()
    }
}
