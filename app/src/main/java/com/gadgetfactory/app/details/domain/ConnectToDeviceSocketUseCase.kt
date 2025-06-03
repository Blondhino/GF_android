package com.gadgetfactory.app.details.domain

import arrow.core.Either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.details.domain.model.SensorMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json

class ConnectToDeviceSocketUseCase(
    private val client: HttpClient,
) {
    operator fun invoke(
        userId: String,
    ): Either<NetworkError, Flow<SensorMessage>> = try {
        val flow = callbackFlow {
            try {
                client.webSocket(
                    urlString = "wss://api.factory-gadget.com/main-channel?roomId=$userId&memberId=android",
                ) {
                    send(Frame.Text("Hello"))

                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val json = frame.readText()
                                try {
                                    val message = Json.decodeFromString<SensorMessage>(json)
                                    trySend(message)
                                } catch (e: Exception) {
                                    // log parse error but don't cancel socket
                                }
                            }

                            is Frame.Close -> {
                                close()
                            }

                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                close(e)
            }
            awaitClose { /* cleanup if needed */ }
        }

        Either.Right(flow)
    } catch (e: Exception) {
        Either.Left(NetworkError.UnknownError(e.message.orEmpty()))
    }
}
