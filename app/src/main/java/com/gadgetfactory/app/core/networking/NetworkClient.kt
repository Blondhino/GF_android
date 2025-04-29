package com.gadgetfactory.app.core.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

fun buildHttpClient() = HttpClient(OkHttp) {
    getClientConfig()
}
