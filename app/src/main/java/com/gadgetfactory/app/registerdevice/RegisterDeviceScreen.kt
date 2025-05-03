package com.gadgetfactory.app.registerdevice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.gadgetfactory.app.registerdevice.ui.BluetoothScannerUiComponent

class RegisterDeviceScreen : Screen {

    @Composable
    override fun Content() = Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // val context = LocalContext.current
        // val viewModel: RegisterDeviceViewModel = koinScreenModel()
        var isActive by remember { mutableStateOf(false) }
        /*val permissionLauncher = rememberLauncherForActivityResult(
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
         */
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            BluetoothScannerUiComponent(
                modifier = Modifier.align(Alignment.TopCenter),
                isActive = isActive,
                message = if (isActive) "Looking for compatible devices..." else "",
                buttonText = "Start Scanning",
                onButtonClick = {
                    isActive = !isActive
                },
            )
        }
    }
}
