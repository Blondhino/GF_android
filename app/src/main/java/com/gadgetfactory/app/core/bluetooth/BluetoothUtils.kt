package com.gadgetfactory.app.core.bluetooth

import android.Manifest
import android.os.Build

fun getRequiredBluetoothPermissions(): Array<String> {
    val basicPermissions = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
    )
    val additionPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
        )

        else -> emptyList()
    }
    return (basicPermissions + additionPermissions).toTypedArray()
}
