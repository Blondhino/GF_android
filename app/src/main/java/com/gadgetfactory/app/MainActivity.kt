package com.gadgetfactory.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.gadgetfactory.app.splash.SplashScreen
import com.gadgetfactory.app.ui.global.snack.ObserveSnackMessages
import com.gadgetfactory.app.ui.global.snack.SnackUiMessage
import com.gadgetfactory.app.ui.global.snack.toPayload
import com.gadgetfactory.app.ui.theme.GadgetFactoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent { App() }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveSnackMessages(snackbarHostState)

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { message ->
                SnackUiMessage(
                    payload = message.toPayload(),
                    duration = message.visuals.duration,
                    onCancel = { snackbarHostState.currentSnackbarData?.dismiss() },
                )
            }
        },
    ) { paddingValues ->
        GadgetFactoryTheme(
            modifier = Modifier,
            paddingValues = paddingValues,
        ) {
            Navigator(SplashScreen())
        }
    }
}
