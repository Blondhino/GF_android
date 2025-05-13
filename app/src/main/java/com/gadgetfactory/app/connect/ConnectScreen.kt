package com.gadgetfactory.app.connect

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.gadgetfactory.app.core.bluetooth.scanner.FoundGadget
import com.gadgetfactory.app.core.ui.components.BodySmallText

class ConnectScreen(val gadget: FoundGadget) : Screen {
    @Composable
    override fun Content() {
        BodySmallText("Connect Screen")
    }
}
