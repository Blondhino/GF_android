package com.gadgetfactory.app.password.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.ui.global.snack.SnackbarPayload
import com.gadgetfactory.app.core.ui.global.snack.SnackbarType

class InvalidPasswordSnackbarMapper(
    val dictionary: Dictionary,
) {
    fun map() = SnackbarPayload(
        title = dictionary.getString(R.string.password_screen_invalid_password_title),
        message = dictionary.getString(R.string.password_screen_invalid_password_message),
        type = SnackbarType.ErrorSnackbar,
    )
}
