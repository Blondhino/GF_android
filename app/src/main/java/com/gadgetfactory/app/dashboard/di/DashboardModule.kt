package com.gadgetfactory.app.dashboard.di

import com.gadgetfactory.app.dashboard.data.RoomRepository
import com.gadgetfactory.app.dashboard.data.RoomRepositoryImpl
import com.gadgetfactory.app.dashboard.data.UserRepository
import com.gadgetfactory.app.dashboard.data.UserRepositoryImpl
import com.gadgetfactory.app.dashboard.data.mapper.DashboardHeaderMapper
import com.gadgetfactory.app.dashboard.data.mapper.DashboardUiMapper
import com.gadgetfactory.app.dashboard.data.mapper.DevicesStateMapper
import com.gadgetfactory.app.dashboard.domain.GetDashboardHeader
import com.gadgetfactory.app.dashboard.domain.GetMyDevices
import com.gadgetfactory.app.dashboard.ui.DashboardViewModel
import com.gadgetfactory.app.dashboard.ui.mapper.DeviceUiItemMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gadgetCenterModule = module {
    factoryOf(::DashboardViewModel)
    factoryOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::RoomRepositoryImpl) bind RoomRepository::class
    factoryOf(::GetDashboardHeader)
    factoryOf(::DashboardUiMapper)
    factoryOf(::DashboardHeaderMapper)
    factoryOf(::GetMyDevices)
    factoryOf(::DevicesStateMapper)
    factoryOf(::DeviceUiItemMapper)
}
