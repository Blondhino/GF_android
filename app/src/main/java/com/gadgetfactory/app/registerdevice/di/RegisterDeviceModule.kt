package com.gadgetfactory.app.registerdevice.di

import com.gadgetfactory.app.registerdevice.ui.RegisterDeviceViewModel
import com.gadgetfactory.app.registerdevice.ui.mapper.RegisterDeviceScreenUiMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val registerDeviceModule = module {
    factoryOf(::RegisterDeviceViewModel)
    factoryOf(::RegisterDeviceScreenUiMapper)
}
