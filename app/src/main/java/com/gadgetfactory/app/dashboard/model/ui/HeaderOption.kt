package com.gadgetfactory.app.dashboard.model.ui

import androidx.annotation.DrawableRes
import com.gadgetfactory.app.R

sealed class HeaderOption(@DrawableRes val icon: Int) {
    data object AddDevice : HeaderOption(icon = R.drawable.ic_add)
    data object Notifications : HeaderOption(icon = R.drawable.ic_notification)
    data object EditRooms : HeaderOption(icon = R.drawable.ic_edit)
}
