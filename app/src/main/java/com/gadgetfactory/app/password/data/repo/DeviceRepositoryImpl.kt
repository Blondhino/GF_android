package com.gadgetfactory.app.password.data.repo

import arrow.core.Either
import com.gadgetfactory.app.core.bluetooth.connector.model.DeviceInfo
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.core.networking.safeApiCall
import com.gadgetfactory.app.core.routes.V1
import com.gadgetfactory.app.password.data.model.RegisterDeviceResponse
import com.gadgetfactory.app.password.domain.repo.DeviceDto
import com.gadgetfactory.app.password.domain.repo.DeviceRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.plugins.resources.get as httpGet

class DeviceRepositoryImpl(
    private val client: HttpClient,
) : DeviceRepository {
    override suspend fun registerDevice(deviceInfo: DeviceInfo): Either<NetworkError, RegisterDeviceResponse> =
        safeApiCall {
            client.post(V1.RegisterDevice()) { setBody(deviceInfo) }.body()
        }

    override suspend fun getMyDevices(): Either<NetworkError, List<DeviceDto>> = safeApiCall {
        client.httpGet(V1.MyDevices()).body()
    }
}
