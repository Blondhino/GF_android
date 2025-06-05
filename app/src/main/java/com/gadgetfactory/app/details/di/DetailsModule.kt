package com.gadgetfactory.app.details.di

import com.gadgetfactory.app.details.domain.ConnectToDeviceSocketUseCase
import com.gadgetfactory.app.details.ui.DetailsScreenViewModel
import com.gadgetfactory.app.details.ui.mapper.DetailsScreenHeaderUiMapper
import com.gadgetfactory.app.details.ui.mapper.DetailsScreenUiMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val detailsModule = module {
    factoryOf(::DetailsScreenViewModel)
    factoryOf(::ConnectToDeviceSocketUseCase)
    factoryOf(::DetailsScreenHeaderUiMapper)
    factoryOf(::DetailsScreenUiMapper)
}
