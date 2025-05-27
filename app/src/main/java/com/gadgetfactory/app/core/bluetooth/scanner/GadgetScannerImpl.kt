package com.gadgetfactory.app.core.bluetooth.scanner

import android.annotation.SuppressLint
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
import com.gadgetfactory.app.core.utils.mapGFDeviceImage
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

@SuppressLint("MissingPermission")
class GadgetScannerImpl(
    private val context: Context,
) : GadgetScanner {
    private val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    private var currentScanner: BluetoothLeScanner? = null
    private var currentCallback: ScanCallback? = null
    private var onScanStoppedCallback: (() -> Unit) = {}
    private var scanningScope: ProducerScope<List<FoundGadget>>? = null
    val foundDevices = mutableMapOf<String, FoundGadget>()

    private fun resetScannerState() {
        currentCallback?.let { callback ->
            currentScanner?.stopScan(callback)
        }
        currentScanner = null
        currentCallback = null
        onScanStoppedCallback = {}
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun discoverGadgets(
        scanDuration: Duration,
        onScanStarted: () -> Unit,
        onScanStopped: () -> Unit,
    ): Either<GadgetScannerError, Flow<List<FoundGadget>>> = either {
        checkPermission().bind()
        val scanner = getScanner().bind()
        currentScanner = scanner
        resetScannerState()
        onScanStoppedCallback = onScanStopped
        return callbackFlow {
            scanningScope = this
            val scanCallback = object : ScanCallback() {
                override fun onScanResult(
                    callbackType: Int,
                    result: ScanResult,
                ) {
                    checkPermission().bind()
                    val device = result.device ?: return
                    val name = result.scanRecord?.deviceName.orEmpty()

                    if (name.startsWith(GADGET_FACTORY_PREFIX)) {
                        foundDevices[device.address] = FoundGadget(
                            name = name.substringAfter(GADGET_FACTORY_PREFIX),
                            address = device.address,
                            image = mapGFDeviceImage(name.substringAfter(GADGET_FACTORY_PREFIX)),
                        )
                    }
                    trySend(foundDevices.values.toList())
                }
            }
            currentCallback = scanCallback
            scanner.startScan(scanCallback)
            onScanStarted()
            launch {
                delay(scanDuration)
                scanner.stopScan(scanCallback)
                onScanStopped()
                close()
            }

            awaitClose {
                scanner.stopScan(scanCallback)
                onScanStopped()
                close()
            }
        }.right()
    }

    override fun stopScanning() {
        currentScanner?.stopScan(currentCallback)
        onScanStoppedCallback
        scanningScope?.close()
        resetScannerState()
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
        ensure(adapter.bluetoothLeScanner != null) { ScannerError }
        val scanner = adapter.bluetoothLeScanner
        ensure(scanner != null) { ScannerError }
        scanner
    }
}

private const val GADGET_FACTORY_PREFIX = "GFactory-"
