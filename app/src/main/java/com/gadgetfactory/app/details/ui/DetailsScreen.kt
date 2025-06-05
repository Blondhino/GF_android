package com.gadgetfactory.app.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.ShowHeader
import com.gadgetfactory.app.details.ui.component.DetailsScreenHeaderContent
import com.gadgetfactory.app.details.ui.component.DetailsScreenInfographicItem
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState.Content
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState.Error
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState.Loading
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class DetailsScreen(
    private val userId: String,
    private val deviceName: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: DetailsScreenViewModel =
            koinScreenModel(parameters = { parametersOf(userId, deviceName) })
        val globalUi: GlobalUi = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        val headerState by viewModel.headerUiState.collectAsState()

        LaunchedEffect(Unit) {
            globalUi.emitUiEvent(
                ShowHeader(content = { DetailsScreenHeaderContent(headerState) }),
            )
        }

        when (val state = uiState) {
            is Content -> DetailsScreenContent(uiState = state)
            is Error -> {}
            is Loading -> {
                /* not necessary because header is shown in loading state*/
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsScreenContent(
    uiState: Content,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        uiState.infographics.forEach { infographic ->
            DetailsScreenInfographicItem(
                infographic = infographic,
            )
        }
    }
}
