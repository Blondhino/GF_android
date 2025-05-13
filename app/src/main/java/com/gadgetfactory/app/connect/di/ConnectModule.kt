package com.gadgetfactory.app.connect.di

import com.gadgetfactory.app.connect.ConnectViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val connectModule = module {
    factoryOf(::ConnectViewModel)
}
