package com.gadgetfactory.app.gadgetcenter.di

import com.gadgetfactory.app.gadgetcenter.data.RoomRepository
import com.gadgetfactory.app.gadgetcenter.data.RoomRepositoryImpl
import com.gadgetfactory.app.gadgetcenter.data.UserRepository
import com.gadgetfactory.app.gadgetcenter.data.UserRepositoryImpl
import com.gadgetfactory.app.gadgetcenter.data.mapper.GadgetCenterHeaderMapper
import com.gadgetfactory.app.gadgetcenter.data.mapper.GadgetCenterUiMapper
import com.gadgetfactory.app.gadgetcenter.domain.GetGadgetCenterHeader
import com.gadgetfactory.app.gadgetcenter.ui.GadgetCenterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gadgetCenterModule = module {
    factoryOf(::GadgetCenterViewModel)
    factoryOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::RoomRepositoryImpl) bind RoomRepository::class
    factoryOf(::GetGadgetCenterHeader)
    factoryOf(::GadgetCenterUiMapper)
    factoryOf(::GadgetCenterHeaderMapper)
}
