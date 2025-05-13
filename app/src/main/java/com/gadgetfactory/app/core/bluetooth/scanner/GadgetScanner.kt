package com.gadgetfactory.app.core.bluetooth.scanner

import arrow.core.Either
import com.gadgetfactory.app.ui.components.ImageType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface GadgetScanner {
    fun discoverGadgets(
        scanDuration: Duration = 10.seconds,
        onScanStarted: () -> Unit = {},
        onScanStopped: () -> Unit = {},
    ): Either<GadgetScannerError, Flow<List<FoundGadget>>>
}

sealed interface GadgetScannerError {
    data object PermissionDenied : GadgetScannerError
    data object AdapterError : GadgetScannerError
    data object ScannerError : GadgetScannerError
}

data class FoundGadget(
    val name: String,
    val address: String,
    val image: ImageType.Resource,
)
