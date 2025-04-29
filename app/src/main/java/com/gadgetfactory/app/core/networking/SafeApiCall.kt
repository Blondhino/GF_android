package com.gadgetfactory.app.core.networking

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError.SerializationError
import com.gadgetfactory.app.core.networking.NetworkError.UnknownError
import io.ktor.client.call.NoTransformationFoundException

suspend fun <SuccessModel : Any> safeApiCall(
    apiCall: suspend () -> SuccessModel,
): Either<NetworkError, SuccessModel> = Either.catch {
    apiCall()
}.mapLeft {
    when (it) {
        is NoTransformationFoundException -> SerializationError(it.message.orEmpty())
        else -> UnknownError(it.message.orEmpty())
    }
}
