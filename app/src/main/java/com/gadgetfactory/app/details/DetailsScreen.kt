package com.gadgetfactory.app.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.gadgetfactory.app.core.ui.components.BodyMediumText
import com.gadgetfactory.app.core.ui.global.GlobalUi
import com.gadgetfactory.app.core.ui.global.GlobalUiEvent.HideHeader
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class DetailsScreen(
    private val userId: String,
    private val deviceId: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: DetailsScreenViewModel =
            koinScreenModel(parameters = { parametersOf(userId, deviceId) })
        val messages by viewModel.messages.collectAsState()
        val globalUi: GlobalUi = koinInject()
        LaunchedEffect(Unit) {
            globalUi.tryEmitUiEvent(HideHeader)
        }
        if (messages.data.isEmpty()) {
            BodyMediumText(text = "No messages received yet")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(messages.data.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        BodyMediumText(
                            text = "${messages.data[index].name}:",
                        )

                        Row {
                            BodyMediumText(
                                text = messages.data[index].value,
                            )

                            BodyMediumText(
                                text = messages.data[index].unit,
                            )
                        }
                    }
                }
            }
        }
    }
}
