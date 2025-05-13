package com.gadgetfactory.app.dashboard.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.dashboard.model.network.UserResponse

interface UserRepository {
    suspend fun getUser(): Either<NetworkError, UserResponse>
}
