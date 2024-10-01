package com.gadgetfactory.app.home.di

import com.gadgetfactory.app.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeModule = module {
    factoryOf(::HomeViewModel)
}