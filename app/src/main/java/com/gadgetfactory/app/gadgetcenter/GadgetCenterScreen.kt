package com.gadgetfactory.app.gadgetcenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.auth.domain.usecase.Logout
import com.gadgetfactory.app.auth.ui.AuthScreen
import com.gadgetfactory.app.ui.components.BodyMediumText
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class GadgetCenterScreen : Screen {
    @Composable
    override fun Content() {
        val logout: Logout = koinInject()
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BodyMediumText("Gadget Center")
                Button(
                    onClick = {
                        scope.launch {
                            logout()
                            navigator.replace(AuthScreen())
                        }
                    },
                ) {
                    BodyMediumText("Logout", color = MaterialTheme.colorScheme.surface)
                }
            }
        }
    }
}
