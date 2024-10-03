package com.gadgetfactory.app.core

import com.gadgetfactory.app.ui.global.GlobalUi
import com.gadgetfactory.app.ui.global.GlobalUiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::GlobalUiImpl) bind GlobalUi::class
}
