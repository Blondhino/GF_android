package com.gadgetfactory.app.password.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.ui.global.snack.SnackbarPayload
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType

class ConnectedDeviceSnackbarMapper(
    val dictionary: Dictionary,
) {
    fun map() = SnackbarPayload(
        title = dictionary.getString(R.string.password_screen_successful_title),
        message = dictionary.getString(R.string.password_screen_successful_message),
        type = SnackbarType.SuccessSnackbar,
    )
}
