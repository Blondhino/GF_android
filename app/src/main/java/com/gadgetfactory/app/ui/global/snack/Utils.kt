package com.gadgetfactory.app.ui.global.snack

import androidx.compose.material3.SnackbarData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SnackbarPayload.getMessage() = Json.encodeToString(this)

fun SnackbarData.toPayload(): SnackbarPayload = Json.decodeFromString(this.visuals.message)
