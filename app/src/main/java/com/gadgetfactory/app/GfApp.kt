package com.gadgetfactory.app

import android.app.Application
import com.gadgetfactory.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GfApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GfApp)
            modules(
                appModule,
            )
        }
    }
}
