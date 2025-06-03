package com.gadgetfactory.app.dashboard.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.core.networking.safeApiCall
import com.gadgetfactory.app.core.routes.V1
import com.gadgetfactory.app.dashboard.data.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

class UserRepositoryImpl(
    private val client: HttpClient,
) : UserRepository {
    override suspend fun getUser(): Either<NetworkError, UserResponse> = safeApiCall {
        client.get(V1.GetUser()).body()
    }
}
