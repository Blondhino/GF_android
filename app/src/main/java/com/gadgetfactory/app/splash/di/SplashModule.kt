package com.gadgetfactory.app.splash.di

import com.gadgetfactory.app.splash.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val splashModule = module {
   factoryOf(::SplashViewModel)
}