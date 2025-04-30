package com.gadgetfactory.app.gadgetcenter.data

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.gadgetcenter.model.network.UserResponse

interface UserRepository {
    suspend fun getUser(): Either<NetworkError, UserResponse>
}
