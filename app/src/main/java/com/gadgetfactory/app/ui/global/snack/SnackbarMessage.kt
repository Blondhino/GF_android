package com.gadgetfactory.app.ui.global.snack

import androidx.compose.material3.SnackbarDuration
import kotlinx.serialization.Serializable

data class SnackbarMessage(
    val payload: SnackbarPayload,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onDismiss: () -> Unit = {},
    val action: () -> Unit = {},
)

@Serializable
data class SnackbarPayload(
    val message: String,
    val title: String,
    val actionTitle: String? = null,
    val type: SnackbarType = SnackbarType.WarningSnackbar,
    val cancelable: Boolean = false,
)

@Serializable
sealed interface SnackbarType {
    @Serializable
    data object WarningSnackbar : SnackbarType

    @Serializable
    data object ErrorSnackbar : SnackbarType

    @Serializable
    data object SuccessSnackbar : SnackbarType
}
