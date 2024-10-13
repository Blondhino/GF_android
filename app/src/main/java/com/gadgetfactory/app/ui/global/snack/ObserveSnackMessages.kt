package com.gadgetfactory.app.ui.global.snack

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.gadgetfactory.app.ui.global.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun ObserveSnackMessages(
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    ObserveAsEvents(flow = SnackbarController.messages) { snack ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            when (
                snackbarHostState.showSnackbar(
                    message = snack.payload.getMessage(),
                    duration = snack.duration,
                )
            ) {
                Dismissed -> snack.onDismiss.invoke()
                ActionPerformed -> snack.action?.action?.invoke()
            }
        }
    }
}
