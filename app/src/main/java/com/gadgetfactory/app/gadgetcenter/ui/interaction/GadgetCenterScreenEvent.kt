package com.gadgetfactory.app.gadgetcenter.ui.interaction

import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption

sealed interface GadgetCenterScreenEvent {
    data class RoomSelected(val roomId: String) : GadgetCenterScreenEvent
    data class OnHeaderOptionClicked(val option: HeaderOption) : GadgetCenterScreenEvent
    data object LogoutClicked : GadgetCenterScreenEvent
}
