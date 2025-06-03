package com.gadgetfactory.app.dashboard.domain

import com.gadgetfactory.app.password.domain.repo.DeviceRepository

class GetMyDevices(
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke() = deviceRepository.getMyDevices()
}
