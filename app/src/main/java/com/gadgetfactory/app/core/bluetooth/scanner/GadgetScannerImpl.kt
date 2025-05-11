package com.gadgetfactory.app.core.bluetooth.scanner

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import com.gadgetfactory.app.core.bluetooth.getRequiredBluetoothPermissions
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScannerError.AdapterError
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScannerError.PermissionDenied
import com.gadgetfactory.app.core.bluetooth.scanner.GadgetScannerError.ScannerError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

class GadgetScannerImpl(
    private val context: Context,
) : GadgetScanner {
    private val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

    @RequiresApi(Build.VERSION_CODES.S)
    override fun discoverGadgets(
        scanDuration: Duration,
        onScanStarted: () -> Unit,
        onScanStopped: () -> Unit,
    ): Either<GadgetScannerError, Flow<List<FoundGadget>>> = either {
        checkPermission().bind()
        val scanner = getScanner().bind()
        val foundDevices = mutableMapOf<String, FoundGadget>()
        return callbackFlow {
            val scanCallback = object : ScanCallback() {
                override fun onScanResult(
                    callbackType: Int,
                    result: ScanResult,
                ) {
                    checkPermission().bind()
                    val device = result.device ?: return
                    val name = device.name ?: ""

                    if (name.isNotEmpty()) {
                        foundDevices[device.address] = FoundGadget(
                            address = device.address,
                            name = name,
                        )
                    }
                    trySend(foundDevices.values.toList())
                }
            }

            scanner.startScan(scanCallback)
            onScanStarted()
            launch {
                delay(scanDuration)
                close()
            }

            awaitClose {
                scanner.stopScan(scanCallback)
                onScanStopped()
            }
        }.right()
    }

    private fun checkPermission(): Either<GadgetScannerError, Unit> = either {
        getRequiredBluetoothPermissions().forEach {
            ensure(checkSelfPermission(context, it) == PERMISSION_GRANTED) { PermissionDenied }
        }
    }

    private fun getScanner(): Either<GadgetScannerError, BluetoothLeScanner> = either {
        val adapter = bluetoothManager.adapter
        ensure(adapter != null) { AdapterError }
        ensure(adapter.isEnabled) { AdapterError }
        val scanner = adapter.bluetoothLeScanner
        ensure(scanner != null) { ScannerError }
        scanner
    }
}
