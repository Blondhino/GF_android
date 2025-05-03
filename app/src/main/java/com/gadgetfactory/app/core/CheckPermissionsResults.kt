package com.gadgetfactory.app.core

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission

fun Map<String, @JvmSuppressWildcards Boolean>.checkPermissionsResults(
    context: Context,
    onAllGranted: () -> Unit,
    onSomeDenied: () -> Unit,
    onAnyPermanentlyDenied: () -> Unit,
) {
    context.findActivity()?.let { activity ->
        this.forEach {
            if (checkSelfPermission(context, it.key) != PERMISSION_GRANTED &&
                !shouldShowRequestPermissionRationale(activity, it.key)
            ) {
                onAnyPermanentlyDenied()
                return@let
            }
        }
        if (this.map { checkSelfPermission(context, it.key) == PERMISSION_GRANTED }
                .all { it }
        ) {
            onAllGranted()
        } else {
            onSomeDenied()
        }
    }
}
