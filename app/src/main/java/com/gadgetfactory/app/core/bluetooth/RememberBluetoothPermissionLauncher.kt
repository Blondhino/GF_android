package com.gadgetfactory.app.core.bluetooth

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable

@Composable
fun rememberBluetoothPermissionLauncher(
    context: Context,
    onResult: (BluetoothPermissions) -> Unit,
) =
    rememberLauncherForActivityResult(contract = RequestMultiplePermissions()) {
        it.checkPermissionsResults(
            context,
            onResult = onResult,
        )
    }
