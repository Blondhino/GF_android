package com.gadgetfactory.app.details

import android.util.Log
import arrow.core.Either
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.details.domain.ConnectToDeviceSocketUseCase
import com.gadgetfactory.app.details.domain.model.SensorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val userId: String,
    private val deviceId: String,
    private val connectToDevice: ConnectToDeviceSocketUseCase,
) : ScreenModel {

    val messages = MutableStateFlow(SensorMessage())

    val socketConnection = screenModelScope.launch {
        when (val result = connectToDevice(userId)) {
            is Either.Right -> {
                result.value.collect { msg ->
                    messages.update { msg }
                }
            }

            is Either.Left -> {
                // log error or update error state
                Log.d(
                    "DetailsScreenViewModel",
                    "Socket connection failed for deviceId : $deviceId: ${result.value.message}",
                )
            }
        }
    }
}
