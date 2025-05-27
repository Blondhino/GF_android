package com.gadgetfactory.app.password.di

import com.gadgetfactory.app.password.data.repo.DeviceRepositoryImpl
import com.gadgetfactory.app.password.domain.RegisterCurrentlyConnectedDevice
import com.gadgetfactory.app.password.domain.repo.DeviceRepository
import com.gadgetfactory.app.password.ui.PasswordViewModel
import com.gadgetfactory.app.password.ui.mapper.ConnectedDeviceSnackbarMapper
import com.gadgetfactory.app.password.ui.mapper.InvalidPasswordSnackbarMapper
import com.gadgetfactory.app.password.ui.mapper.PasswordScreenUiMapper
import com.gadgetfactory.app.password.ui.mapper.UnableConnectBackendSnackbarMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val passwordModule = module {
    factoryOf(::PasswordViewModel)
    factoryOf(::PasswordScreenUiMapper)
    factoryOf(::InvalidPasswordSnackbarMapper)
    factoryOf(::DeviceRepositoryImpl) bind DeviceRepository::class
    factoryOf(::RegisterCurrentlyConnectedDevice)
    factoryOf(::UnableConnectBackendSnackbarMapper)
    factoryOf(::ConnectedDeviceSnackbarMapper)
}
