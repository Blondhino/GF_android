package com.gadgetfactory.app.core.networking

sealed class NetworkError(val message: String) {
    data class SerializationError(val errorMessage: String) : NetworkError(errorMessage)
    data class UnknownError(val errorMessage: String) : NetworkError(errorMessage)
}
