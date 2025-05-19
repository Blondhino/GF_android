package com.gadgetfactory.app.core.di

import com.gadgetfactory.app.core.bluetooth.connector.BleConnector
import com.gadgetfactory.app.core.bluetooth.connector.GadgetFactoryBleConnector
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScanner
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScannerImpl
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.dictionary.DictionaryImpl
import com.gadgetfactory.app.core.dictionary.LocalStringResources
import com.gadgetfactory.app.core.dictionary.StringResources
import com.gadgetfactory.app.core.networking.buildHttpClient
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiImpl
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import kotlinx.coroutines.tasks.await
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::GlobalUiImpl) bind GlobalUi::class
    singleOf(::DictionaryImpl) bind Dictionary::class
    singleOf(::LocalStringResources) bind StringResources::class
    single { buildHttpClient().configureToken(get()) }
    factoryOf(::GadgetScannerImpl) bind GadgetScanner::class
    singleOf(::GadgetFactoryBleConnector) bind BleConnector::class
}

private fun HttpClient.configureToken(auth: FirebaseAuth): HttpClient = apply {
    plugin(HttpSend).intercept { request ->
        try {
            val token = auth.currentUser?.getIdToken(true)?.await()?.token.orEmpty()
            request.header(TOKEN_HEADER, token)
            execute(request)
        } catch (e: Exception) {
            execute(request)
        }
    }
}

private const val TOKEN_HEADER = "token"
