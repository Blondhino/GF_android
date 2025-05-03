package com.gadgetfactory.app.gadgetcenter.ui.interaction

sealed interface GadgetCenterViewEffect {
    data object NavigateToAuthScreen : GadgetCenterViewEffect
    data object NavigateToRegisterDeviceScreen : GadgetCenterViewEffect
    data object HideHeader : GadgetCenterViewEffect
}
