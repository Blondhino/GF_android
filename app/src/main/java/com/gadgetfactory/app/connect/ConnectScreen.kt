package com.gadgetfactory.app.connect

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import com.gadgetfactory.app.core.bluetooth.connector.GadgetFactoryBleConnector
import com.gadgetfactory.app.core.ui.components.BodySmallText
import kotlinx.coroutines.launch

class ConnectScreen(val gadgetAddress: String) : Screen {
    @Composable
    override fun Content() = Box(modifier = Modifier.fillMaxSize()) {
        val bleConnector = GadgetFactoryBleConnector(LocalContext.current)
        val scope = rememberCoroutineScope()
        BodySmallText("Connect Screen")

        LaunchedEffect(Unit) {
            bleConnector.connectWithDevice(
                address = gadgetAddress,
                onError = {},
                onConnected = {
                    scope.launch {
                        bleConnector.scanWiFiNetworks().collect {
                            Log.d("BLE_RESULT", "Collected WiFi: $it")
                        }
                    }
                },
            )
        }

        Button(modifier = Modifier.align(Alignment.Center), onClick = { bleConnector.disconnectCurrentDevice() }) {
            BodySmallText("Disconnect")
        }
    }
}
