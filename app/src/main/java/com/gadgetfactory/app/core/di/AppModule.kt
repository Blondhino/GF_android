package com.gadgetfactory.app.core.di

import com.gadgetfactory.app.auth.di.authModule
import com.gadgetfactory.app.core.coreModule
import com.gadgetfactory.app.dashboard.di.gadgetCenterModule
import com.gadgetfactory.app.scan.di.registerDeviceModule
import com.gadgetfactory.app.splash.di.splashModule

val appModule = listOf(
    splashModule,
    coreModule,
    authModule,
    gadgetCenterModule,
    registerDeviceModule,
)
