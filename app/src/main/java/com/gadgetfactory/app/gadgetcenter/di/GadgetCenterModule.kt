package com.gadgetfactory.app.gadgetcenter.di

import com.gadgetfactory.app.gadgetcenter.GadgetCenterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val gadgetCenterModule = module {
    factoryOf(::GadgetCenterViewModel)
}
