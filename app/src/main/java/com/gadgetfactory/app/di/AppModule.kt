package com.gadgetfactory.app.di

import com.gadgetfactory.app.auth.di.authModule
import com.gadgetfactory.app.core.coreModule
import com.gadgetfactory.app.home.di.homeModule
import com.gadgetfactory.app.splash.di.splashModule

val appModule = listOf(
    homeModule,
    splashModule,
    coreModule,
    authModule,
)
