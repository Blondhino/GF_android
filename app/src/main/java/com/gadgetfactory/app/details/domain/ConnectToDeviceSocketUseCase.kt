package com.gadgetfactory.app.details.domain

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.details.domain.model.SensorMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame.Text
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.util.UUID

class ConnectToDeviceSocketUseCase(
    private val client: HttpClient,
) {
    operator fun invoke(
        userId: String,
    ): Flow<Either<NetworkError, SensorMessage>> = flow {
        try {
            client.webSocket(urlString = generateConnectionUrl(userId)) {
                for (frame in incoming) {
                    when {
                        frame is Text -> {
                            frame.parseFrame().fold(
                                ifLeft = { error -> emit(Either.Left(error)) },
                                ifRight = { message -> emit(Either.Right(message)) },
                            )
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            emit(Either.Left(NetworkError.UnknownError(exception.message.orEmpty())))
        }
    }

    private fun generateConnectionUrl(userId: String) = SOCKET_URL
        .replace(USER_ID_PLACEHOLDER, userId)
        .replace(MEMBER_ID_PLACEHOLDER, UUID.randomUUID().toString())

    private fun Text.parseFrame(): Either<NetworkError, SensorMessage> = Either.catch {
        Json.decodeFromString<SensorMessage>(this.readText())
    }.mapLeft { exception ->
        NetworkError.SerializationError(exception.message.orEmpty())
    }
}

private const val USER_ID_PLACEHOLDER = "userId"
private const val MEMBER_ID_PLACEHOLDER = "memberUid"
private const val SOCKET_URL =
    "wss://api.factory-gadget.com/main-channel?roomId=$USER_ID_PLACEHOLDER&memberId=$MEMBER_ID_PLACEHOLDER"
