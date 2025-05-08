package com.gadgetfactory.app.core

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import com.gadgetfactory.app.core.BluetoothPermissions.AllGranted
import com.gadgetfactory.app.core.BluetoothPermissions.AnyPermanentlyDenied
import com.gadgetfactory.app.core.BluetoothPermissions.SomeDenied

fun Map<String, @JvmSuppressWildcards Boolean>.checkPermissionsResults(
    context: Context,
    onResult: (BluetoothPermissions) -> Unit,
) {
    if (!isBluetoothEnabled()) {
        onResult(BluetoothPermissions.AdapterTurnedOff)
        return
    }
    context.findActivity()?.let { activity ->
        this.forEach {
            if (checkSelfPermission(context, it.key) != PERMISSION_GRANTED &&
                !shouldShowRequestPermissionRationale(activity, it.key)
            ) {
                onResult(AnyPermanentlyDenied)
                return@let
            }
        }
        if (this.map { checkSelfPermission(context, it.key) == PERMISSION_GRANTED }
                .all { it }
        ) {
            onResult(AllGranted)
        } else {
            onResult(SomeDenied)
        }
    }
}

private fun isBluetoothEnabled(): Boolean {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    return bluetoothAdapter?.isEnabled == true
}

sealed interface BluetoothPermissions {
    data object AllGranted : BluetoothPermissions
    data object SomeDenied : BluetoothPermissions
    data object AnyPermanentlyDenied : BluetoothPermissions
    data object AdapterTurnedOff : BluetoothPermissions
}
