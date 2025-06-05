package com.gadgetfactory.app.password.domain.repo

import arrow.core.Either
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceInfo
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.password.data.model.RegisterDeviceResponse

interface DeviceRepository {
    suspend fun registerDevice(deviceInfo: DeviceInfo): Either<NetworkError, RegisterDeviceResponse>
    suspend fun getMyDevices(): Either<NetworkError, List<DeviceDto>>
}
