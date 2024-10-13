package com.gadgetfactory.app.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.auth.ui.AuthScreen
import com.gadgetfactory.app.gadgetcenter.GadgetCenterScreen

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SplashViewModel = koinScreenModel()
        val startDestination by viewModel.startDestination.collectAsState()

        startDestination?.let {
            when (it) {
                StartDestination.Auth -> navigator.replace(AuthScreen())
                StartDestination.GadgetCenter -> navigator.replace(GadgetCenterScreen())
            }
        }
    }
}
