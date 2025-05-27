package com.gadgetfactory.app.password.domain

import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository
import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.password.domain.repo.DeviceRepository
import kotlinx.coroutines.flow.take

class RegisterCurrentlyConnectedDevice(
    private val deviceConnector: BleConnector,
    private val repository: DeviceRepository,
    private val authRepository: AuthenticationRepository,
) {
    suspend operator fun invoke() {
        deviceConnector.getDeviceInfo().take(1).collect { deviceInfo ->
            val apiResult = repository.registerDevice(deviceInfo)
            apiResult.onRight { apiResult ->
                authRepository.getCurrentUser().map { user ->
                    deviceConnector.provideUserIdAndApiKey(id = user.id, apiKey = apiResult.apiKey)
                }
            }
        }
    }
}
