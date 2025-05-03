package com.gadgetfactory.app.registerdevice

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.core.checkPermissionsResults
import com.gadgetfactory.app.ui.components.BodySmallText

class RegisterDeviceScreen : Screen {

    @Composable
    override fun Content() = Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        val viewModel: RegisterDeviceViewModel = koinScreenModel()
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = RequestMultiplePermissions(),
        ) {
            it.checkPermissionsResults(
                context = context,
                onAllGranted = { Log.d("permRes", "All permissions granted") },
                onSomeDenied = { Log.d("permRes", "Some permissions denied") },
                onAnyPermanentlyDenied = {
                    Log.d("permRes", "Some permissions permanently denied")
                },
            )
        }

        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {
                permissionLauncher.launch(viewModel.getRequiredPermissions())
            },
        ) {
            BodySmallText("Start Scan")
        }
    }
}
