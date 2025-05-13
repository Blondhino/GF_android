package com.gadgetfactory.app.scan.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.scan.ui.components.ScanScreenContent
import com.gadgetfactory.app.scan.ui.components.ScanViewEffectHandler
import org.koin.compose.koinInject

class ScanScreen : Screen {

    @Composable
    override fun Content() = Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val viewModel: ScanViewModel = koinScreenModel()
        val globalUi: GlobalUi = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        ScanViewEffectHandler(
            viewModel = viewModel,
            globalUi = globalUi,
            uiState = uiState,
            context = LocalContext.current,
        )

        ScanScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEvent = viewModel::onEvent,
        )
    }
}
