package com.gadgetfactory.app.details.di

import com.gadgetfactory.app.details.DetailsScreenViewModel
import com.gadgetfactory.app.details.domain.ConnectToDeviceSocketUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val detailsModule = module {
    factoryOf(::DetailsScreenViewModel)
    factoryOf(::ConnectToDeviceSocketUseCase)
}
