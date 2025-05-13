package com.gadgetfactory.app.scan.di

import com.gadgetfactory.app.scan.ui.ScanViewModel
import com.gadgetfactory.app.scan.ui.mapper.ScanScreenUiMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val registerDeviceModule = module {
    factoryOf(::ScanViewModel)
    factoryOf(::ScanScreenUiMapper)
}
